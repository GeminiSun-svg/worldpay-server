package com.worldpay.service.impl;
import com.worldpay.utils.GeTuiPushUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.system.MessageTemplateDao;
import com.worldpay.dao.system.NoticeDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.response.DicEntity;
import com.worldpay.entity.system.MessageTemplateEntity;
import com.worldpay.entity.system.NoticeEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.NoticeStateEnums;
import com.worldpay.enums.NoticeTypeEnums;
import com.worldpay.service.NoticeService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.MessageFormatUtils;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Service
@CrossOrigin
@Transactional
public class NoticeServiceImpl extends BaseApiService implements NoticeService{
	
	@Autowired
	private NoticeDao NoticeDao;

    @Autowired
    private AppUserDao appUserDao;
    
    @Autowired
    private MessageTemplateDao messageTemplateDao;
    
	@Autowired
	private GeTuiPushUtils pushUtils;

	@Override
	@SysLogAnnotation(module = "通知接口", type = "POST", remark = "查询所有通知")
	public ResponseBase findAll(@RequestBody NoticeEntity entity) {
		try {
			String language = LanguageContext.getLanguage();
			entity.setLanguage(language);
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<NoticeEntity> list = NoticeDao.findList(entity);
			PageInfo<NoticeEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


    @Override
    @SysLogAnnotation(module = "通知接口", type = "POST", remark = "新增通知")
    public ResponseBase add(@RequestBody NoticeEntity entity) {
        try {
            if(entity.getLanguage() == null){
                return setResultError(Constants.NULL_LANGUAGE);
            }
            entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
            entity.setTypeName(NoticeTypeEnums.getName(entity.getType()));
            entity.setStatus(NoticeStateEnums.NORMAL.getIndex());
            entity.setStatusName(NoticeStateEnums.NORMAL.getName());
            GenericityUtil.setDate(entity);
            NoticeDao.insert(entity);
            return setResultSuccess(I18nUtil.getMessage("base_success"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @SysLogAnnotation(module = "通知接口", type = "POST", remark = "编辑通知信息")
    public ResponseBase update(@RequestBody NoticeEntity entity) {
        try {
            if(entity.getId() == null){
                return setResultError(I18nUtil.getMessage("base_data_null"));
            }
            NoticeEntity infoEntity = NoticeDao.selectById(entity.getId());
            if(infoEntity != null) {
            	NoticeDao.updateById(entity);
                return setResultSuccess();
            }else {
                return setResultError(I18nUtil.getMessage("base_data_null"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @SysLogAnnotation(module = "通知接口", type = "GET", remark = "删除通知")
    public ResponseBase delete(Integer id) {
        try {
            NoticeEntity entity = NoticeDao.selectById(id);
            if(entity != null) {
                NoticeDao.deleteById(id);
                return setResultSuccess(I18nUtil.getMessage("base_success"));
            }else {
                return setResultError(I18nUtil.getMessage("base_error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


	@Override
	@SysLogAnnotation(module = "通知接口", type = "get", remark = "发送通知")
	public ResponseBase send(Integer id) {
		try {
			NoticeEntity entity = NoticeDao.selectById(id);
			if(entity != null) {
//				List<String> uidList = appUserDao.finduIdList();
				//推送消息
				pushUtils.pushAll(entity.getTitle(), entity.getContent(), null);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "通知接口", type = "POST", remark = "查询消息通知列表")
	public ResponseBase findList(@RequestBody NoticeEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<NoticeEntity> list = NoticeDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					AppUserEntity appUserEntity = appUserDao.findByUid(list.get(i).getUid());
					list.get(i).setUserData(appUserEntity);
				}
			}
			PageInfo<NoticeEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	public ResponseBase findById(Integer id) {
		NoticeEntity entity = NoticeDao.selectById(id);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}
	
	/**
	 * @category 统一用户站内消息
	 * @param entity
	 * @return
	 */
	public ResponseBase addSys(NoticeEntity entity) {
		try {
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			entity.setTypeName(NoticeTypeEnums.getName(entity.getType()));
			entity.setStatus(NoticeStateEnums.NORMAL.getIndex());
			entity.setStatusName(NoticeStateEnums.NORMAL.getName());
			GenericityUtil.setDate(entity);
			NoticeDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	/**
	 * @category 新增站内消息
	 * @param uid
	 * @param msgCode
	 * @param args
	 */
	public void addMsg(String uid,Integer msgCode,Object... args){
		//多语言消息
		try {
			List<DicEntity> languageList = LanguageEnums.getList();
			if(languageList != null && languageList.size() > 0) {
				for (int i = 0; i < languageList.size(); i++) {
					MessageTemplateEntity templateEntity = messageTemplateDao.findOne(msgCode, languageList.get(i).getName());
					if(templateEntity != null) {
						NoticeEntity noticeEntity = new NoticeEntity();
						noticeEntity.setUid(uid);
						noticeEntity.setType(NoticeTypeEnums.SYSTEM_NOTIFICATIONS.getIndex());
						noticeEntity.setTypeName(NoticeTypeEnums.SYSTEM_NOTIFICATIONS.getName());
						noticeEntity.setLanguage(languageList.get(i).getName());
						noticeEntity.setLanguageName(LanguageEnums.getLable(languageList.get(i).getName()));
						noticeEntity.setTitle(templateEntity.getMsgTitles());
						noticeEntity.setIntro(templateEntity.getMsgTitles());
						String message = MessageFormatUtils.format(
								templateEntity.getMessageInfo(),args);
						noticeEntity.setContent(message);
						noticeEntity.setStatus(NoticeStateEnums.NORMAL.getIndex());
						noticeEntity.setStatusName(NoticeStateEnums.NORMAL.getName());
						GenericityUtil.setDate(noticeEntity);
						NoticeDao.insert(noticeEntity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}

	/**
	 * @category 新增站内消息
	 * @param uid
	 * @param msgCode
	 * @param args
	 */
	@Async("asyncExecutor") // 指定线程池，可省略参数
	public void addMsgAsync(String uid,Integer msgCode,Object... args) {
	    try {
	        // 调用原有的同步方法
	        this.addMsg(uid,msgCode,args);
	    } catch (Exception e) {
	        // 捕获异常，防止线程异常终止
	        e.printStackTrace();
	    }
	}
	
	
}
