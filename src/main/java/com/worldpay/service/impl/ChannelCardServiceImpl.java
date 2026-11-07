package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.worldpay.api.response.ChannelCardResponse;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.channel.ChannelCardDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.channel.ChannelCardEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.service.ChannelCardService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.JwtUtil;
import com.worldpay.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class ChannelCardServiceImpl extends BaseApiService implements ChannelCardService {

    @Autowired
    private ChannelCardDao channelCardDao;

    @Autowired
    private CardDao cardDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;

    @Autowired
    private AppUserIntiterDao appUserIntiterDao;
    
    @Autowired
    private ChannelUserDao channelUserDao;


    @Override
    public ResponseBase findAll(ChannelCardEntity entity) {
        Integer channelId = entity.getChannelId();
        List<ChannelCardResponse> list;
        if (channelId != null) {
            List<String> coverUids = collectCoverInviterUserUidsRecursive(String.valueOf(channelId));
            coverUids.add(String.valueOf(channelId));
            list = channelCardDao.selectChannelCardByUserIds(coverUids, entity.getCardType(),entity.getAcctive(),entity.getCardId());
        } else {
            list = channelCardDao.selectChannelCardById(null,entity.getCardType(),entity.getAcctive(),entity.getCardId());
        }
        list.forEach(channelCardEntity -> channelCardEntity.setCardEntity(cardDao.selectOne(new QueryWrapper<CardEntity>().eq("uuid", channelCardEntity.getCardUuid()))));
        List<ChannelCardResponse> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<ChannelCardResponse> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info);
    }

    @Override
    public ResponseBase appFindAll(ChannelCardEntity entity, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", appUserEntity.getUid()));
        List<ChannelCardResponse> list = new ArrayList<>();
        if (appChannelIntiterEntity != null){
            String channelId = appChannelIntiterEntity.getChannelUid();
            List<String> coverUids = collectCoverInviterUserUidsRecursive(String.valueOf(channelId));
            coverUids.add(String.valueOf(channelId));
            list = channelCardDao.selectChannelCardByUserIds(coverUids,entity.getCardType(), entity.getAcctive(),entity.getCardId());
            list.forEach(channelCardEntity -> channelCardEntity.setCardEntity(cardDao.selectOne(new QueryWrapper<CardEntity>().eq("uuid", channelCardEntity.getCardUuid()))));
        }
        List<ChannelCardResponse> channelCardResponses = GenericityUtil.Page(list,entity.getPageNumber(),entity.getPageSize());
        PageInfo<ChannelCardResponse> info = new PageInfo<>(channelCardResponses);
        info.setTotal(list.size());
        return setResultSuccess(info);
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

    @Override
    public ResponseBase findById(Integer id) {
        ChannelCardResponse channelCardResponse = channelCardDao.selectCardInfoById(id);
        channelCardResponse.setCardEntity(cardDao.selectOne(new QueryWrapper<CardEntity>().eq("uuid", channelCardResponse.getCardUuid())));
        return setResultSuccess(channelCardResponse);
    }


    @Override
    public ResponseBase update(ChannelCardEntity entity) {
        ChannelCardEntity channelCardEntity = channelCardDao.selectById(entity.getId());
        if (channelCardEntity == null) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        String cardUuid = channelCardEntity.getCardUuid();
        CardEntity cardEntity = cardDao.selectOne(new QueryWrapper<CardEntity>().eq("uuid", cardUuid));
        if (cardEntity == null || cardEntity.getOpenCardCost() == null) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        Double openFee = entity.getOpenFee();
        if (openFee == null) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        if (openFee.compareTo(cardEntity.getOpenCardCost()) < 0) {
            return setResultError(I18nUtil.getMessage("open_fee_is_low_system", cardEntity.getOpenCardCost()));
        }
        channelCardEntity.setOpenFee(openFee);
        channelCardEntity.setGmtModified(new Date());
        int flag = channelCardDao.updateById(channelCardEntity);
        if (flag <= 0) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        return setResultSuccess(I18nUtil.getMessage("base_success"));

    }

	@Override
	public ResponseBase oneClickUpdate() {
		try {
			// 更新银行卡信息
			List<CardEntity> cardEntities = cardDao.selectList(null);
			List<ChannelUserEntity> userEntities = channelUserDao.selectList(null);
			if (cardEntities != null && cardEntities.size() > 0 && userEntities != null && userEntities.size() > 0) {
				for (int i = 0; i < cardEntities.size(); i++) {
					for (int j = 0; j < userEntities.size(); j++) {
						ChannelCardEntity cardEntity = channelCardDao.findOne(userEntities.get(j).getId(),
								cardEntities.get(i).getUuid());
						if (cardEntity == null) {
							ChannelCardEntity channelCard = new ChannelCardEntity();
							channelCard.setCardId(cardEntities.get(i).getId());
							channelCard.setCardUuid(cardEntities.get(i).getUuid());
							channelCard.setCardType(cardEntities.get(i).getBankCardNature());
							channelCard.setChannelId(userEntities.get(j).getId());
							channelCard.setOpenFee(cardEntities.get(i).getOpenCardCost());
							channelCard.setRatesFee(cardEntities.get(i).getRechargeFee());
							GenericityUtil.setDate(channelCard);
							channelCardDao.insert(channelCard);
						}

					}
				}
			}
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
}
