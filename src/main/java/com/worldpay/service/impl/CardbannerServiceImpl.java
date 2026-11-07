package com.worldpay.service.impl;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.dao.card.CardbannerDao;
import com.worldpay.entity.card.CardBannerEntity;
import com.worldpay.enums.FilePathEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.NoticeStateEnums;
import com.worldpay.service.CardbannerService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.S3FileUploadUtil;

@Service
@Transactional
@CrossOrigin
public class CardbannerServiceImpl extends BaseApiService implements CardbannerService{
	
	@Autowired
	private CardbannerDao CardbannerDao;


    @Override
    @SysLogAnnotation(module = "轮播图接口", type = "POST", remark = "查询所有轮播图")
    public ResponseBase findAll(@RequestBody CardBannerEntity entity) {
        try {
            entity.setStatus(0);
            PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
            List<CardBannerEntity> list = CardbannerDao.findList(entity);
            if(list != null && list.size() > 0) {
            	for (int i = 0; i < list.size(); i++) {
					list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
				}
            }
            PageInfo<CardBannerEntity> info = new PageInfo<>(list);
            return setResultSuccess(info);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


	@Override
	@SysLogAnnotation(module = "轮播图接口", type = "POST", remark = "新增轮播图")
	public ResponseBase add(String dataStr, MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				CardBannerEntity entity = JSONObject.parseObject(dataStr, CardBannerEntity.class);
				if(file != null) {
					String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.CARD.getName());
					if(fileUrl != null && fileUrl.length() > 0) {
						entity.setImgUrl(fileUrl);
					}	
				}
				GenericityUtil.setDate(entity);
				entity.setStatus(NoticeStateEnums.NORMAL.getIndex());
				CardbannerDao.insert(entity);
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
	@SysLogAnnotation(module = "轮播图接口", type = "POST", remark = "编辑轮播图")
	public ResponseBase update(String dataStr, MultipartFile file) {
		try {
			if (dataStr != null && dataStr.length() > 0) {
				CardBannerEntity entity = JSONObject.parseObject(dataStr, CardBannerEntity.class);
				if (entity.getId() == null) {
					return setResultError(Constants.NULL_ID);
				}
				CardBannerEntity CardbannerEntity = CardbannerDao.selectById(entity.getId());
				if (CardbannerEntity != null) {
					if(file != null) {
						String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.CARD.getName());
						if(fileUrl != null && fileUrl.length() > 0) {
							entity.setImgUrl(fileUrl);
						}	
					}
					entity.setGmtModified(new Date());
					CardbannerDao.updateById(entity);
					return setResultSuccess();
				} else {
					return setResultError(I18nUtil.getMessage("base_data_null"));
				}
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "轮播图接口", type = "get", remark = "删除轮播图")
	public ResponseBase delete(Integer id) {
		try {
            CardBannerEntity entity = CardbannerDao.selectById(id);
            if(entity != null) {
                CardbannerDao.deleteById(id);
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
	@SysLogAnnotation(module = "轮播图接口", type = "get", remark = "查询轮播图信息")
	public ResponseBase findById(String id) {
		try {
			List<CardBannerEntity> list = CardbannerDao.findById(id);
			return setResultSuccess(list, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

    @Override
    @SysLogAnnotation(module = "轮播图接口", type = "POST", remark = "轮播图停用启用")
    public ResponseBase changeStatus(Integer id, Integer statusType) {
        try {
            CardBannerEntity entity = CardbannerDao.selectById(id);
            if(entity != null) {
                entity.setStatus(statusType);
                CardbannerDao.updateById(entity);
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
    @SysLogAnnotation(module = "轮播图接口", type = "GET", remark = "查询正常状态下轮播图")
    public ResponseBase findNormalInfo() {
        List<CardBannerEntity> list = CardbannerDao.findNormalInfo(0);
        return setResultSuccess(JSON.toJSON(list));
    }

    
}
