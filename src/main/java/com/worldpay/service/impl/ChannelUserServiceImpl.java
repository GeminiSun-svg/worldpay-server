package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.ChannelUserAddEntity;
import com.worldpay.api.response.ChannelUserInfoEntity;
import com.worldpay.api.response.UserEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.brokerage.ChannelLevelDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.channel.ChannelCardDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.system.IpWhitelistDao;
import com.worldpay.dao.system.SysRoleDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.channel.ChannelCardEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.system.IpWhitelistEntity;
import com.worldpay.entity.system.SysRoleEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.enums.RoleKeyEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.service.ChannelUserService;
import com.worldpay.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.worldpay.constants.UserConstants.ROLE_KEY;

@Service
@Transactional
public class ChannelUserServiceImpl extends BaseApiService implements ChannelUserService {

    @Autowired
    private ChannelUserDao channelUserDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private IpWhitelistDao ipWhitelistDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private ChannelCardDao channelCardDao;

    @Autowired
    private ChannelLevelDao channelLevelDao;

    @Autowired
    private CardDao cardDao;
    @Autowired
    private AppUserIntiterDao appUserIntiterDao;
    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;
    @Autowired
    private AppUserDao appUserDao;

    @Override
    @SysLogAnnotation(module = "渠道商查询", type = "POST", remark = "渠道商查询")
    public ResponseBase findAll(ChannelUserEntity entity,HttpServletRequest request) {
        UserEntity userEntity = getUserEntity(request);
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        List<ChannelUserEntity> list = null;
        if (userEntity.getRoleKey().equals(RoleKeyEnums.CHANNEL.getName())){
            List<String> coverUids = collectCoverInviterUserUidsRecursive(userEntity.getUid());
            list = coverUids.isEmpty() ? new ArrayList<>() : channelUserDao.selectChannelUsersByUserIds(coverUids,entity);
        }else {
            list = channelUserDao.selectchannelUsers(entity);
        }

        PageInfo<ChannelUserEntity> info = new PageInfo<>(list);
        return setResultSuccess(info);
    }

    @Override
    public ResponseBase appFindAll(ChannelUserEntity entity, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        if (appUserEntity == null){
            return setResultError(I18nUtil.getMessage("user.account_error"));
        }
        // 获取app用户与渠道商的绑定关系
        AppChannelIntiterEntity appChannelIntiter = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>()
                .eq("app_id", appUserEntity.getId())
                .eq("app_uid", appUserEntity.getUid()));
        List<String> coverUids = collectCoverInviterUserUidsRecursive(appChannelIntiter.getChannelUid());
        List<ChannelUserEntity> list = coverUids.isEmpty() ? new ArrayList<>() : channelUserDao.selectChannelUsersByUserIds(coverUids,entity);
        PageInfo<ChannelUserEntity> info = new PageInfo<>(list);
        return setResultSuccess(info);
    }

    @Override
    public ResponseBase appFindById(String id) {
        ChannelUserEntity channelUserEntity = channelUserDao.selectChannelInfoById(id);
        IpWhitelistEntity ipBySetUser = ipWhitelistDao.findIpBySetUser(channelUserEntity.getUserId());
        if (ipBySetUser != null){
            channelUserEntity.setIpWhite(ipBySetUser.getIpSite());
        }
        return setResultSuccess(channelUserEntity);
    }

    @Override
    public ResponseBase findChannelUserInfo() {
        List<ChannelUserInfoEntity> channelUserInfoEntities = channelUserDao.findChannelUserInfo();
        return setResultSuccess(channelUserInfoEntities);
    }

    @Override
    @SysLogAnnotation(module = "新增渠道商", type = "POST", remark = "新增渠道商")
    public ResponseBase add(ChannelUserAddEntity entity, HttpServletRequest request) {
        try {
            // 获取当前渠道商用户ID（channelUid）
            String channelUid = getChannelUidFromRequest(request);

            // 校验当前登录用户权限（不能添加等级高于自己的用户，佣金率不能超过自己）
            validateCurrentUserPermission(channelUid, entity);

            // 检查账号是否已存在
            if (checkAccountNotExist(entity)){
                return setResultSuccess(I18nUtil.getMessage("base_success"));
            }

            // 创建系统用户
            SysUserEntity sysUser = createSysUser(entity);

            // 绑定角色
            bindRoleToUser(sysUser.getId(), getChannelRoleId());

            // 添加IP白名单（如有）
            addIpWhitelist(entity, sysUser);

            // 创建渠道用户
            ChannelUserEntity channelUser = createChannelUser(entity, sysUser);

            // 为渠道用户复制所有卡片信息
            copyCardsToChannel(channelUser.getId());

            // 关联App用户或渠道商（邀请人关系）
            linkInviterRelation(sysUser, channelUser, request);
        } catch (Exception e) {
            return setResultError(e.getMessage());
        }

        return setResultSuccess(I18nUtil.getMessage("base_success"));
    }

    /**
     * 从请求中获取当前登录用户的渠道商ID（channelUid）
     */
    private String getChannelUidFromRequest(HttpServletRequest request) {
        UserEntity userEntity = getUserEntity(request);
        if (userEntity != null) {
            return userEntity.getUid();
        }

        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUser = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        if (appUser == null) {
            throw new RuntimeException(I18nUtil.getMessage("user.not_null"));
        }

        AppChannelIntiterEntity relation = appChannelIntiterDao.selectOne(
                new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", appUser.getUid()));
        if (relation == null) {
            throw new RuntimeException(I18nUtil.getMessage("channel_nonentity"));
        }
        return relation.getChannelUid();
    }

    /**
     * 校验当前登录用户是否有权限添加下级用户（等级不能高于自己，佣金率不能超过自己）
     */
    private void validateCurrentUserPermission(String channelUid, ChannelUserAddEntity entity) {
        ChannelUserEntity currentUser = channelUserDao.selectOne(
                new QueryWrapper<ChannelUserEntity>().eq("user_id", channelUid));
        if (currentUser == null) {
            return; // 无当前用户记录时跳过校验（原逻辑允许）
        }

        if (currentUser.getLevelNo() >= entity.getLevelNo()) {
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }

        Double currentRate = currentUser.getCommissionRate();
        if (currentRate == null) {
            throw new RuntimeException(I18nUtil.getMessage("channel_rate_is_not_null"));
        }
        if (entity.getCommissionRate() > currentRate) {
            throw new RuntimeException(I18nUtil.getMessage("current_channel_rate",currentRate));
        }
    }

    /**
     * 检查账号是否已存在（acctive且状态为正常）
     */
    private boolean checkAccountNotExist(ChannelUserAddEntity entity) {
        // 1. 查询 sys_user 是否存在（根据 acctive，即登录账号）
        SysUserEntity existSysUser = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>()
                .eq("acctive", entity.getAcctive()));
        if (existSysUser == null) {
            return false; // 用户不存在，需要走完整新增流程
        }

        // 2. 如果用户状态为正常，说明账号未被注销 -> 仍然是冲突（不允许重复添加正常用户）
        if (Objects.equals(existSysUser.getUserState(), UserStateEnums.NORMAL.getIndex())) {
            throw new RuntimeException(I18nUtil.getMessage("user.account_exist"));
        }

        // 3. 用户状态为注销 -> 恢复 sys_user
        existSysUser.setUserState(UserStateEnums.NORMAL.getIndex());
        existSysUser.setGoogleSecretkey(entity.getGoogleSecretkey());
        existSysUser.setPassword(DigestUtils.md5DigestAsHex(entity.getPassword().getBytes()));
        existSysUser.setCreateTime(LocalDate.now().toString()); // 注意：原逻辑覆盖创建时间，按需保留
        sysUserDao.updateById(existSysUser);

        // 4. 查询对应的 channel_user（注意字段名：可能是 user_id，不是 userId）
        ChannelUserEntity existChannelUser = channelUserDao.selectOne(
                new QueryWrapper<ChannelUserEntity>().eq("user_id", existSysUser.getId()));

        if (existChannelUser != null && existChannelUser.getDeleteStatus() == 1) {
            // 5. 恢复渠道用户（逻辑删除标记改为0，并更新关键字段）
            existChannelUser.setDeleteStatus(UserStateEnums.NORMAL.getIndex());
            existChannelUser.setUsername(entity.getUsername());
            existChannelUser.setCommissionRate(entity.getCommissionRate());
            existChannelUser.setLevelNo(entity.getLevelNo());
            ChannelLevelEntity level = channelLevelDao.selectOne(
                    new QueryWrapper<ChannelLevelEntity>().eq("level_no", entity.getLevelNo()));
            if (level != null){
                existChannelUser.setLevelName(level.getLevelName());
            }
            existChannelUser.setGmtModified(new Date());
            // 注意：可能需要重新生成邀请码？原逻辑未处理，若需要可自行补充
            channelUserDao.updateById(existChannelUser);
        }
        // 6. 执行完两个修改后，任务结束，通知主流程不再新增
        return true;
    }

    /**
     * 创建系统用户并返回完整实体
     */
    private SysUserEntity createSysUser(ChannelUserAddEntity entity) {
        SysUserEntity sysUser = new SysUserEntity();
        sysUser.setUsername(entity.getUsername());
        sysUser.setPassword(DigestUtils.md5DigestAsHex(entity.getPassword().getBytes()));
        sysUser.setGoogleSecretkey(entity.getGoogleSecretkey());
        sysUser.setAcctive(entity.getAcctive());
        sysUser.setTel(entity.getTel());
        sysUser.setUserState(UserStateEnums.NORMAL.getIndex());

        int rows = sysUserDao.insert(sysUser);
        if (rows <= 0) {
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }

        // 重新查询获取自增ID
        return sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("acctive", entity.getAcctive()));
    }

    /**
     * 获取渠道商角色ID（常量ROLE_KEY对应的角色）
     */
    private Integer getChannelRoleId() {
        SysRoleEntity role = sysRoleDao.findRoleIdByRoleKey(ROLE_KEY);
        if (role == null) {
            throw new RuntimeException(I18nUtil.getMessage("role.not_found"));
        }
        return role.getRoleId();
    }

    /**
     * 绑定用户与角色
     */
    private void bindRoleToUser(Integer userId, Integer roleId) {
        sysUserDao.addUserOrRole(userId, roleId);
    }

    /**
     * 添加IP白名单（如果提供了ipWhite）
     */
    private void addIpWhitelist(ChannelUserAddEntity entity, SysUserEntity sysUser) {
        if (StringUtils.isBlank(entity.getIpWhite())) {
            return;
        }

        IpWhitelistEntity whitelist = new IpWhitelistEntity();
        whitelist.setIpSite(entity.getIpWhite());
        whitelist.setGmtModified(new Date());
        whitelist.setSetTime(new Date());
        whitelist.setSetUserName(entity.getUsername());
        whitelist.setSetUser(sysUser.getId());

        int rows = ipWhitelistDao.insert(whitelist);
        if (rows <= 0) {
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }
    }

    /**
     * 创建渠道用户记录
     */
    private ChannelUserEntity createChannelUser(ChannelUserAddEntity entity, SysUserEntity sysUser) {
        ChannelLevelEntity level = channelLevelDao.selectOne(
                new QueryWrapper<ChannelLevelEntity>().eq("level_no", entity.getLevelNo()));
        if (level == null) {
            throw new RuntimeException(I18nUtil.getMessage("channel.level.not_found"));
        }

        ChannelUserEntity channelUser = new ChannelUserEntity();
        channelUser.setUserId(sysUser.getId());
        channelUser.setUsername(sysUser.getUsername());
        channelUser.setCommissionRate(entity.getCommissionRate());
        channelUser.setLevelNo(entity.getLevelNo());
        channelUser.setCardOpeningDeductionFee(level.getPrepaidFees());
        channelUser.setLevelName(level.getLevelName());
        channelUser.setInvitationCode(RandomSuffixInviteCodeUtil.generateUniqueCode(sysUser.getId(), 4, 6));
        channelUser.setSetTime(new Date());
        channelUser.setGmtModified(new Date());

        int rows = channelUserDao.insert(channelUser);
        if (rows <= 0) {
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }
        return channelUser;
    }

    /**
     * 为指定渠道用户复制所有可用卡片信息到渠道卡片表
     */
    private void copyCardsToChannel(Integer channelUserId) {
        List<CardEntity> allCards = cardDao.findList(null);
        if (allCards == null || allCards.isEmpty()) {
            return;
        }

        for (CardEntity card : allCards) {
            ChannelCardEntity channelCard = new ChannelCardEntity();
            channelCard.setChannelId(channelUserId);
            channelCard.setCardId(card.getId());
            channelCard.setCardUuid(card.getUuid());
            channelCard.setCardType(card.getBankCardNature());
            channelCard.setOpenFee(card.getOpenCardCost());
            channelCard.setRatesFee(card.getRechargeFee());
            channelCard.setSetTime(new Date());
            channelCard.setGmtModified(new Date());

            int rows = channelCardDao.insert(channelCard);
            if (rows <= 0) {
                throw new RuntimeException(I18nUtil.getMessage("base_error"));
            }
        }
    }

    /**
     * 关联邀请人关系（App用户或渠道商）
     */
    private void linkInviterRelation(SysUserEntity sysUser, ChannelUserEntity channelUser, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUser = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));

        String inviterChannelUid = null;
        if (appUser != null) {
            AppChannelIntiterEntity relation = appChannelIntiterDao.selectOne(
                    new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", appUser.getUid()));
            if (relation != null) {
                inviterChannelUid = relation.getChannelUid();
            }
        } else {
            UserEntity userEntity = getUserEntity(request);
            if (userEntity != null && RoleKeyEnums.CHANNEL.getName().equals(userEntity.getRoleKey())) {
                inviterChannelUid = userEntity.getUid();
            }
        }

        if (inviterChannelUid != null) {
            addAppUserIntiter(sysUser, channelUser, inviterChannelUid);
        }
    }


    /**
     * 新增绑定关系
     * @param sysUserEntity
     * @param channelUserEntity
     * @param userId
     */
    public void  addAppUserIntiter(SysUserEntity sysUserEntity,ChannelUserEntity channelUserEntity,String userId){
        //邀请人信息
        ChannelUserEntity intiterChannelUser = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id",userId));
        //被邀请人
        ChannelUserEntity coverInviterChannelUser = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id",sysUserEntity.getId()));
        // 增加绑定关系
        AppUserIntiterEntity appUserIntiterEntity = new AppUserIntiterEntity();
        appUserIntiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
        appUserIntiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
        appUserIntiterEntity.setInviterUser(intiterChannelUser.getId());
        appUserIntiterEntity.setInviterUserUid(String.valueOf(intiterChannelUser.getUserId()));
        appUserIntiterEntity.setCoverInviterUser(coverInviterChannelUser.getId());
        appUserIntiterEntity.setCoverInviterUserUid(String.valueOf(coverInviterChannelUser.getUserId()));
        appUserIntiterEntity.setSetTime(new Date());
        appUserIntiterEntity.setGmtModified(new Date());
        appUserIntiterDao.insert(appUserIntiterEntity);
    }

    /**
     * 根据邀请人 uid 递归收集下级被邀请人 uid，遇到 C 端邀请人（intiterUserType=1）时停止向下递归
     */
    private List<String> collectCoverInviterUserUidsRecursive(String inviterUid) {
        Set<String> uids = new LinkedHashSet<>();
        collectCoverInviterUserUidsRecursive(inviterUid, uids);
        return new ArrayList<>(uids);
    }

    private void collectCoverInviterUserUidsRecursive(String inviterUid, Set<String> uids) {
        List<AppUserIntiterEntity> inviterList = appUserIntiterDao.findInviterUserListNoTime(inviterUid);
        if (inviterList == null || inviterList.isEmpty()) {
            return;
        }
        for (AppUserIntiterEntity record : inviterList) {
            String coverUid = record.getCoverInviterUserUid();
            if (StringUtils.isBlank(coverUid)) {
                continue;
            }
            uids.add(coverUid);
            if (IntiterUserTypeEnums.APP_USER.getIndex().equals(record.getIntiterUserType())) {
                continue;
            }
            collectCoverInviterUserUidsRecursive(coverUid, uids);
        }
    }

    public UserEntity getUserEntity(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        return sysUserDao.findRoleKeyByUserName(username);
    }

    @Override
    @SysLogAnnotation(module = "渠道商更新", type = "POST", remark = "渠道商更新")
    public ResponseBase update(ChannelUserAddEntity entity) {
        try {
        	SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("id", entity.getUserId()));
            sysUserEntity.setUsername(entity.getUsername());
            sysUserEntity.setTel(entity.getTel());
            int i = sysUserDao.updateById(sysUserEntity);
            if (i <= 0) {
                throw new RuntimeException(I18nUtil.getMessage("base_error"));
            }
            ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", entity.getUserId()));
            //添加流水比例校验 判断其是够有上级
            AppUserIntiterEntity coverInviterUser = appUserIntiterDao.selectOne(new QueryWrapper<AppUserIntiterEntity>().eq("cover_inviter_user_uid", entity.getUserId()));
            if (coverInviterUser != null){
                //获取上级的uid
                String inviterUserUid = coverInviterUser.getInviterUserUid();
                Double commissionRate = channelUserDao.selectCommissionRate(inviterUserUid);
                if (entity.getCommissionRate() > commissionRate){
                    return setResultError(I18nUtil.getMessage("current_channel_rate",commissionRate));
                }
            }
            channelUserEntity.setCommissionRate(entity.getCommissionRate());
            channelUserEntity.setUsername(entity.getUsername());
            channelUserEntity.setGmtModified(new Date());
            channelUserEntity.setLevelNo(entity.getLevelNo());
            channelUserEntity.setLevelName(channelLevelDao.selectOne(new QueryWrapper<ChannelLevelEntity>().eq("level_no",entity.getLevelNo())).getLevelName());
            int updateById = channelUserDao.updateById(channelUserEntity);
            if (updateById <= 0) {
                throw new RuntimeException(I18nUtil.getMessage("base_error"));
            }
            String ipWhite = entity.getIpWhite();
            if (StringUtils.isNotBlank(ipWhite)) {
                IpWhitelistEntity whitelistEntity = ipWhitelistDao.selectOne(new QueryWrapper<IpWhitelistEntity>().eq("set_user", sysUserEntity.getId()));
                if(whitelistEntity != null) {
                    whitelistEntity.setIpSite(ipWhite);
                    whitelistEntity.setGmtModified(new Date());
                    ipWhitelistDao.updateById(whitelistEntity);
                }else {
                	IpWhitelistEntity ipWhitelistEntity = new IpWhitelistEntity();
                	ipWhitelistEntity.setIpSite(ipWhite);
                	ipWhitelistEntity.setSetUser(sysUserEntity.getId());
                	ipWhitelistEntity.setSetUserName(sysUserEntity.getUsername());
                	GenericityUtil.setDate(ipWhitelistEntity);
                	ipWhitelistDao.insert(ipWhitelistEntity);
                }
            }
            return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }

    @Override
    @SysLogAnnotation(module = "修改渠道商密码", type = "POST", remark = "修改渠道商密码")
    public ResponseBase updatePwd(ChannelUserAddEntity entity) {
        Integer userId = entity.getUserId();
        String password = entity.getPassword();
        if (userId == null || StringUtils.isBlank(password)) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        sysUserDao.updatePwd(userId, DigestUtils.md5DigestAsHex(password.getBytes()));
        return setResultSuccess(I18nUtil.getMessage("base_success"));
    }

    @Override
    @SysLogAnnotation(module = "删除渠道商", type = "GET", remark = "删除渠道商")
    public ResponseBase deleteChannelUser(int userId) {
        //删除sys_user表中数据
        sysUserDao.updateState(userId, UserStateEnums.LOGOUT.getIndex());
        //删除channel_user表数据
        channelUserDao.updateState(userId);
        return setResultSuccess(I18nUtil.getMessage("base_success"));
    }

    @Override
    public ResponseBase findChannelLevel(HttpServletRequest request) {
        UserEntity userEntity = getUserEntity(request);
        List<ChannelLevelEntity> channelLevelEntities = channelLevelDao.selectList(new QueryWrapper<ChannelLevelEntity>().eq("delete_state","1"));
        if (userEntity.getRoleKey().equals(RoleKeyEnums.CHANNEL.getName())){
            //如果是渠道商查询渠道商等级
            ChannelLevelEntity channelLevel = channelLevelDao.findChannelLevel(userEntity.getUid());
            List<ChannelLevelEntity> collect = channelLevelEntities.stream()
                    .filter(channelLevelEntity -> channelLevelEntity.getLevelNo() > channelLevel.getLevelNo())
                    .sorted(Comparator.comparing(ChannelLevelEntity::getLevelNo))
                    .collect(Collectors.toList());
            return setResultSuccess(collect);
        }
        return setResultSuccess(channelLevelEntities);
    }

    @Override
    public ResponseBase appFindChannelLevel(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                new QueryWrapper<AppChannelIntiterEntity>()
                        .eq("app_uid", appUserEntity.getUid()));
        List<ChannelLevelEntity> channelLevelEntities = channelLevelDao.selectList(
                new QueryWrapper<ChannelLevelEntity>()
                        .eq("delete_state","1"));
        //如果是渠道商查询渠道商等级
        ChannelLevelEntity channelLevel = channelLevelDao.findChannelLevel(appChannelIntiterEntity.getChannelUid());
        List<ChannelLevelEntity> collect = channelLevelEntities.stream()
                .filter(channelLevelEntity -> channelLevelEntity.getLevelNo() > channelLevel.getLevelNo())
                .sorted(Comparator.comparing(ChannelLevelEntity::getLevelNo))
                .collect(Collectors.toList());
        return setResultSuccess(collect);
    }
}
