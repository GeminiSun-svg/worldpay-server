package com.worldpay.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.product.ProductDao;
import com.worldpay.dao.product.ProductOrderDao;
import com.worldpay.dao.product.ProductTypeDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.product.ProductEntity;
import com.worldpay.entity.product.ProductOrderEntity;
import com.worldpay.entity.product.ProductTypeEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.FilePathEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.OrderStateEnums;
import com.worldpay.enums.ProductStateEnums;
import com.worldpay.enums.PublicEnums;
import com.worldpay.enums.TradeTypeEnums;
import com.worldpay.enums.TransCurrencyEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.query.ProductPayEntity;
import com.worldpay.service.ProductService;
import com.worldpay.utils.DateUtil;
import com.worldpay.utils.FinanceUtils;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.OrderCodeFactory;
import com.worldpay.utils.S3FileUploadUtil;

import lombok.Synchronized;

@Service
@Transactional
public class ProductServiceImpl extends BaseApiService implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductTypeDao productTypeDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private ProductOrderDao productOrderDao;
	
	@Autowired
	private WalletLogDao walletLogDao;

	@Override
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "新增理财产品")
	public ResponseBase add(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				ProductEntity entity = JSONObject.parseObject(dataStr, ProductEntity.class);
				if(entity.getProductStateId() == null) {
					entity.setProductStateId(ProductStateEnums.DOWN_ZH.getIndex());
					entity.setProductState(ProductStateEnums.DOWN_ZH.getName());
				}else {
					entity.setProductState(ProductStateEnums.getName(entity.getProductStateId()));
				}
				entity.setDeleteState(PublicEnums.ONE.getIndex());
				if(entity.getProductTypeId() != null) {
					ProductTypeEntity typeEntity = productTypeDao.selectById(entity.getProductTypeId());
					entity.setProductType(typeEntity.getName());
					entity.setWhetherCurrent(typeEntity.getWhetherCurrent());
				}
				if(file != null) {
					String logUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.PRODUCT.getDatabaseName());
					if(logUrl!= null && logUrl.length() > 0) {
						entity.setCoverImg(logUrl);
					}
				}
				if(entity.getInterestRate() != null) {
					entity.setInterestRate(entity.getInterestRate()/100);
				}
				GenericityUtil.setDate(entity);
				productDao.insert(entity);
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
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "编辑理财产品")
	public ResponseBase update(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				ProductEntity entity = JSONObject.parseObject(dataStr, ProductEntity.class);
				ProductEntity productEntity = productDao.selectById(entity.getId());
				if(productEntity != null) {
					if(file != null) {
						String logUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.PRODUCT.getDatabaseName());
						if(logUrl!= null && logUrl.length() > 0) {
							entity.setCoverImg(logUrl);
						}
					}
					if(entity.getProductTypeId() != null) {
						ProductTypeEntity typeEntity = productTypeDao.selectById(entity.getProductTypeId());
						entity.setProductType(typeEntity.getName());
						entity.setWhetherCurrent(typeEntity.getWhetherCurrent());
					}
					if(entity.getInterestRate() != null) {
						entity.setInterestRate(entity.getInterestRate()/100);
					}
					productDao.updateById(entity);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
				return setResultError(I18nUtil.getMessage("base_error"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	

	@Override
	@SysLogAnnotation(module = "理财产品", type = "get", remark = "上下架")
	public ResponseBase OnAndOff(Integer id, Integer type,String language) {
		try {
			ProductEntity entity = productDao.selectById(id);
			if(entity != null) {
				entity.setProductState(ProductStateEnums.getName(type));
				entity.setProductStateId(type);
				productDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "理财产品列表")
	public ResponseBase findList(ProductEntity entity) {
		try {
			entity.setLanguage(LanguageContext.getLanguage());
			entity.setProductStateId(ProductStateEnums.SUPERIOR_ZH.getIndex());
			entity.setDeleteState(PublicEnums.ONE.getIndex());
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<ProductEntity> list = productDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setProductState(ProductStateEnums.getLanguageName(list.get(i).getProductStateId(), LanguageContext.getLanguage()));
				}
			}
			PageInfo<ProductEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	@Synchronized
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "购买理财产品")
	public ResponseBase pay(ProductPayEntity entity) {
		try {
			AppUserEntity userData = appUserDao.findByUid(entity.getUid());
			if(userData != null) {
				if(userData.getPayPassword() != null && userData.getPayPassword().length() > 0) {
					if (!DigestUtils.md5DigestAsHex((entity.getPayPassword()).getBytes())
							.equals(userData.getPayPassword())) {
						return setResultError(I18nUtil.getMessage("pay_password_error"));
					}
				}else {
					return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_null"), null);
				}
				//产品信息
				ProductEntity product = productDao.selectById(entity.getProductId());
				if(product == null) {
					return setResultError(I18nUtil.getMessage("base_error"));
				}
				if(!ProductStateEnums.SUPERIOR_ZH.getIndex().equals(product.getProductStateId())) {
					return setResultError(I18nUtil.getMessage("product_state_check"));
				}
				if(entity.getPayMonery() < product.getInitialMonery()) {
					return setResultError(I18nUtil.getMessage("product_amount_check"));
				}
				//钱包余额是否充足
				Double temp = userData.getWalletBalance(); // - userData.getFreezeBalance();
				if(temp - entity.getPayMonery() < 0) {
					return setResult(Constants.HTTP_RES_CODE_601, I18nUtil.getMessage("wallet_Balance_null"), null);
				}
				//生成认购订单
				ProductOrderEntity orderEntity = new ProductOrderEntity();
				orderEntity.setUid(entity.getUid());
				orderEntity.setProductId(product.getId());
				orderEntity.setOrderNum(OrderCodeFactory.getOrderCode(Long.parseLong(entity.getUid())));
				orderEntity.setOrderState(OrderStateEnums.POSTED.getName());
				orderEntity.setOrderMonery(entity.getPayMonery());
				orderEntity.setProductName(product.getProductName());
				orderEntity.setProductImg(product.getCoverImg());
				orderEntity.setProductType(product.getProductType());
				orderEntity.setFundsType(product.getFundsType());
				orderEntity.setProductTime(product.getProductTime());
				orderEntity.setInterestRate(product.getInterestRate());
				orderEntity.setWhetherCurrent(product.getWhetherCurrent());
				orderEntity.setPayNum(entity.getPayNum());
				Date date = new Date();
				orderEntity.setPayTime(date);
				if(product.getWhetherCurrent().equals(PublicEnums.TOW.getIndex())) {//定期产品
					//定期产品设置到期时间
					orderEntity.setExpireTime(DateUtil.addDays(date, product.getProductTime()));
					//定期产品计算预计收益
					Double profit = FinanceUtils.calculateProfit(entity.getPayMonery(), product.getInterestRate(), product.getProductTime());
					orderEntity.setEstimateIncome(profit);
				}
				GenericityUtil.setDate(orderEntity);
				productOrderDao.insert(orderEntity);
				//冻结金额
				Double balance = userData.getWalletBalance() == null? 0 : userData.getWalletBalance();
				userData.setWalletBalance(userData.getWalletBalance() - entity.getPayMonery());
				userData.setFreezeBalance(userData.getFreezeBalance() + entity.getPayMonery());
				appUserDao.updateById(userData);
				//新增钱包冻结记录
				this.addWalletOpenFreeze(userData, entity.getPayMonery(),balance);
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
	@SysLogAnnotation(module = "理财产品", type = "get", remark = "查询用户持仓资产总额")
	public ResponseBase userAssets(String uid) {
		try {
			Double monery = productOrderDao.findUserSum(uid,OrderStateEnums.POSTED.getName());
			return setResultSuccess(monery, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "查询订单")
	public ResponseBase orderList(ProductOrderEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<ProductOrderEntity> list = productOrderDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					AppUserEntity appUserEntity = appUserDao.findByUid(list.get(i).getUid());
					if (appUserEntity != null) {
						appUserEntity.setPayPassword(null);
						appUserEntity.setUserPassword(null);
						list.get(i).setAppUser(appUserEntity);
					}
					ProductEntity productEntity = productDao.selectById(list.get(i).getProductId());
					list.get(i).setProduct(productEntity);
				}
			}
			PageInfo<ProductOrderEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "理财产品", type = "get", remark = "结束订单(活期产品结束持有)")
	public ResponseBase orderFinish(Integer id) {
		try {
			ProductOrderEntity orderEntity = productOrderDao.selectById(id);
			if (orderEntity != null) {
				AppUserEntity appUserEntity = appUserDao.findByUid(orderEntity.getUid());
				if(appUserEntity == null) {
					return setResultError(I18nUtil.getMessage("base_error"));
				}
				if (!OrderStateEnums.POSTED.getName().equals(orderEntity.getOrderState())) {
					return setResultError(I18nUtil.getMessage("product_order_error"));
				}
				if (orderEntity.getWhetherCurrent().equals(PublicEnums.TOW.getIndex())) { // 定期产品
					Date now = new Date();
					Date expireTime = orderEntity.getExpireTime();
					if (expireTime != null && expireTime.after(now)) {
						return setResultError(I18nUtil.getMessage("product_expire_no"));
					}
				}
				//修改订单内容
				Integer days = DateUtil.getDayDisparityNum(orderEntity.getPayTime(),new Date()); // 持有天数
				Double profit = FinanceUtils.calculateProfit(orderEntity.getOrderMonery(),
						orderEntity.getInterestRate(), days);//收益
				orderEntity.setActualIncome(profit);
				orderEntity.setOrderState(OrderStateEnums.FINISH.getName());
				orderEntity.setFinishTime(new Date());
				productOrderDao.updateById(orderEntity);
				//解冻余额，增加收益
				Double balance = appUserEntity.getWalletBalance() == null ? 0 : appUserEntity.getWalletBalance();//现钱包余额
				Double money = orderEntity.getOrderMonery() + profit;//订单金额+收益
				appUserEntity.setWalletBalance(appUserEntity.getWalletBalance() + money);
				appUserEntity.setFreezeBalance(appUserEntity.getFreezeBalance() - orderEntity.getOrderMonery());
				appUserDao.updateById(appUserEntity);
				//增加钱包记录
				this.addWalletOpenReturns(appUserEntity, money,balance);
				return setResultSuccess(orderEntity,I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "理财产品", type = "post", remark = "理财产品列表")
	public ResponseBase pcFindList(ProductEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			entity.setDeleteState(PublicEnums.ONE.getIndex());
			List<ProductEntity> list = productDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
				}
			}
			PageInfo<ProductEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(Integer id) {
		try {
			ProductEntity entity = productDao.selectById(id);
			if(entity != null) {
				entity.setDeleteState(PublicEnums.TOW.getIndex());
				productDao.updateById(entity);
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
	public ResponseBase findById(Integer id) {
		try {
			ProductOrderEntity entity = productOrderDao.selectById(id);
			if(entity != null) {
				AppUserEntity appUserEntity = appUserDao.findByUid(entity.getUid());
				appUserEntity.setPayPassword(null);
				appUserEntity.setUserPassword(null);
				entity.setAppUser(appUserEntity);
				ProductEntity productEntity = productDao.selectById(entity.getProductId());
				entity.setProduct(productEntity);
				if(entity.getWhetherCurrent().equals(PublicEnums.TOW.getIndex())) {//定期产品
					//定期产品计算预计收益
					Double profit = FinanceUtils.calculateProfit(entity.getOrderMonery(), entity.getInterestRate(), entity.getProductTime());
					entity.setEstimateIncome(profit);
				}else {
					Integer days = DateUtil.getDayDisparityNum(entity.getPayTime(),new Date()); // 持有天数
					Double profit = FinanceUtils.calculateProfit(entity.getOrderMonery(),
							entity.getInterestRate(), days);//预计收益
					entity.setEstimateIncome(profit);
					entity.setActualIncome(profit);
				}
				return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	 public WalletLogEntity addWalletOpenFreeze(AppUserEntity userEntity, Double money, Double balance) {
			try {
				WalletLogEntity walletLogEntity = new WalletLogEntity();
				walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
				walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
				walletLogEntity.setUserId(userEntity.getId());
				walletLogEntity.setUid(userEntity.getUid());
				walletLogEntity.setTradeType(TradeTypeEnums.EXPENDITURE.getIndex());
				walletLogEntity.setTitle(WalletTitleTypeEnums.PAY_PRODUCT_THAW.getCnName());
				walletLogEntity.setPrimevalMoney(balance);
				walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
				walletLogEntity.setRealMoney(BigDecimal.valueOf(money));
				walletLogEntity.setFormName(userEntity.getUserEmail());
				walletLogEntity.setFormAccount(userEntity.getUserEmail());
				walletLogEntity.setToName(userEntity.getUserEmail());
				walletLogEntity.setToAccount(userEntity.getUserEmail());
				walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
				walletLogEntity.setOperateType(WalletTitleTypeEnums.PAY_PRODUCT_THAW.getIndex());
				GenericityUtil.setDate(walletLogEntity);
				walletLogDao.insert(walletLogEntity);
				return walletLogEntity;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

	
	 public WalletLogEntity addWalletOpenReturns(AppUserEntity userEntity, Double money, Double balance) {
			try {
				WalletLogEntity walletLogEntity = new WalletLogEntity();
				walletLogEntity.setOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
				walletLogEntity.setOutOrderNo(OrderCodeFactory.getOrderCode(Long.parseLong(userEntity.getUid())));
				walletLogEntity.setUserId(userEntity.getId());
				walletLogEntity.setUid(userEntity.getUid());
				walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
				walletLogEntity.setTitle(WalletTitleTypeEnums.PAY_INVESTMENT_RETURNS.getCnName());
				walletLogEntity.setPrimevalMoney(userEntity.getWalletBalance());
				walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
				walletLogEntity.setRealMoney(BigDecimal.valueOf(money));
				walletLogEntity.setFormName(userEntity.getUserEmail());
				walletLogEntity.setFormAccount(userEntity.getUserEmail());
				walletLogEntity.setToName(userEntity.getUserEmail());
				walletLogEntity.setToAccount(userEntity.getUserEmail());
				walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
				walletLogEntity.setOperateType(WalletTitleTypeEnums.PAY_INVESTMENT_RETURNS.getIndex());
				GenericityUtil.setDate(walletLogEntity);
				walletLogDao.insert(walletLogEntity);
				return walletLogEntity;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}


}
