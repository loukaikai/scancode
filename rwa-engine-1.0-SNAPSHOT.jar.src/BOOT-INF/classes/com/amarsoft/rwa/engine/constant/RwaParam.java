/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*     */ 
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum RwaParam
/*     */   implements ICodeEnum
/*     */ {
/*  10 */   ID("id", "id", "ID"),
/*  11 */   SIZE("size", "size", "SIZE"),
/*  12 */   PROCESS_STATUS("process_status", "processStatus", "计算处理状态"),
/*  13 */   EXPOSURE_ID("exposure_id", "exposureId", "暴露ID"),
/*  14 */   ABS_EXPOSURE_ID("abs_exposure_id", "absExposureId", "资产证券化暴露ID"),
/*  15 */   ABS_PRODUCT_ID("abs_product_id", "absProductId", "资产证券化产品ID"),
/*  16 */   CONTRACT_ID("contract_id", "contractId", "合同ID"),
/*  17 */   MITIGATION_ID("mitigation_id", "mitigationId", "缓释物ID"),
/*  18 */   CLIENT_ID("client_id", "clientId", "参与主体ID"),
/*  19 */   COLLATERAL_ID("collateral_id", "collateralId", "押品ID"),
/*  20 */   GUARANTEE_ID("guarantee_id", "guaranteeId", "保证ID"),
/*  21 */   FORMULA_NO("formula_no", "formulaNo", "计算公式流水号"),
/*  22 */   NETTING_ID("netting_id", "nettingId", "净额结算ID"),
/*  23 */   ORG_ID("org_id", "orgId", "所属机构ID"),
/*  24 */   INDUSTRY_ID("industry_id", "industryId", "所属行业代码"),
/*     */   
/*  26 */   REAL_ESTATE_TYPE("real_estate_type", "realEstateType", "房地产类型"),
/*  27 */   IS_PRUDENTIAL("is_prudential", "isPrudential", "是否审慎"),
/*  28 */   INITIAL_VALUE("initial_value", "initialValue", "初始认定价值"),
/*     */   
/*  30 */   LTV("ltv", "ltv", "贷款价值比"),
/*  31 */   RESIDENTIAL_VALUE("residential_value", "residentialValue", "居住用房地产价值"),
/*  32 */   RLTV("rltv", "rltv", "居住用LTV"),
/*  33 */   COMMERCIAL_VALUE("commercial_value", "commercialValue", "商用房地产价值"),
/*  34 */   CLTV("cltv", "cltv", "商用LTV"),
/*     */   
/*  36 */   APPROACH("approach", "approach", "计算方法"),
/*  37 */   MITIGATE_ASSET("mitigate_asset", "mitigateAsset", "资产缓释方法设置"),
/*     */   
/*  39 */   IS_RETAIL_EXPOSURE("is_retail_exposure", "isRetailExposure", "是否零售风险暴露"),
/*  40 */   IS_ALONE("is_alone", "isAlone", "是否单挂"),
/*     */   
/*  42 */   IS_DVP("is_dvp", "isDvp", "是否货款对付模式"),
/*  43 */   DELAYED_TRAD_DAYS("delayed_trad_days", "delayedTradDays", "延迟交易天数"),
/*     */   
/*  45 */   EXPOSURE_BELONG("exposure_belong", "exposureBelong", "暴露所属标识"),
/*     */   
/*  47 */   EXPOSURE_TYPE_WA("exposure_type_wa", "exposureTypeWa", "权重法暴露类型"),
/*  48 */   EXPOSURE_MAIN_TYPE_IRB("exposure_main_type_irb", "exposureMainTypeIrb", "内评法暴露大类"),
/*  49 */   EXPOSURE_TYPE_IRB("exposure_type_irb", "exposureTypeIrb", "内评法暴露类型"),
/*     */   
/*  51 */   IS_CURRENCY_MISMATCH("is_currency_mismatch", "isCurrencyMismatch", "是否币种错配"),
/*  52 */   INCOME_CURRENCY("income_currency", "incomeCurrency", "收入币种"),
/*     */   
/*  54 */   GUARANTOR_EXPO_TYPE("guarantor_expo_type", "guarantorExpoType", "保证人暴露类型"),
/*  55 */   SIB_FLAG("sib_flag", "sibFlag", "系统重要性银行标识"),
/*  56 */   IS_VOLATILITY("is_volatility", "isVolatility", "是否波动性较大"),
/*  57 */   DEFAULT_FLAG("default_flag", "defaultFlag", "违约标识"),
/*  58 */   INSTRUMENTS_TYPE("instruments_type", "instrumentsType", "金融工具类型"),
/*  59 */   CLAIMS_LEVEL("claims_level", "claimsLevel", "债权级别"),
/*  60 */   ORIGINAL_MATURITY("original_maturity", "originalMaturity", "原始期限"),
/*  61 */   RESIDUAL_MATURITY("residual_maturity", "residualMaturity", "剩余期限"),
/*  62 */   BOOK_TYPE("book_type", "bookType", "账簿类别"),
/*  63 */   ASSET_TYPE("asset_type", "assetType", "资产类别"),
/*  64 */   START_DATE("start_date", "startDate", "起始日期"),
/*     */   
/*  66 */   AMP_FLAG("amp_flag", "ampFlag", "资产管理产品标识"),
/*  67 */   AMP_APPROACH("amp_approach", "ampApproach", "资产管理产品计量方法"),
/*  68 */   AMP_LR("amp_lr", "ampLr", "资产管理产品杠杆率"),
/*  69 */   IS_TP_CALC("is_tp_calc", "isTpCalc", "是否第三方计算"),
/*  70 */   NET_ASSET("net_asset", "netAsset", "净资产"),
/*  71 */   MAX_INVEST_RATIO("max_invest_ratio", "maxInvestRatio", "最高投资比例"),
/*  72 */   INVESTMENT_RATIO("investment_ratio", "investmentRatio", "投资比例"),
/*  73 */   ASSET_TYPE_ABA("asset_type_aba", "assetTypeAba", "授权基础法资产类型"),
/*  74 */   ABA_ASSET_ID("aba_asset_id", "abaAssetId", "授权基础法资产编号"),
/*  75 */   MTM("mtm", "mtm", "盯市价值/重置成本"),
/*  76 */   AMP_UNDER_ASSET_RST("amp_under_asset_rst", "ampUnderAssetRst", "资产管理产品底层资产结果"),
/*  77 */   AB_CP("ab_cp", "abCp", "交易对手资产余额"),
/*  78 */   EAD_CP("ead_cp", "eadCp", "交易对手风险暴露"),
/*  79 */   RWA_CP("rwa_cp", "rwaCp", "交易对手风险加权资产"),
/*  80 */   CVA("cva", "cva", "信用估值调整"),
/*     */   
/*  82 */   PROVIDE_MITIGATION_TYPE("provide_mitigation_type", "provideMitigationType", "提供合格缓释工具类型"),
/*     */   
/*  84 */   EXPOSURE_RPT_ITEM_WA("exposure_rpt_item_wa", "exposureRptItemWa", "权重法暴露报表项目"),
/*  85 */   OFF_RPT_ITEM_WA("off_rpt_item_wa", "offRptItemWa", "表外报表项目"),
/*  86 */   SPRV_RATING("sprv_rating", "sprvRating", "监管评级"),
/*     */   
/*  88 */   IS_CONSOLIDATED_SUB("is_consolidated_sub", "isConsolidatedSub", "是否并表子公司"),
/*     */   
/*  90 */   CLIENT_TYPE("client_type", "clientType", "参与主体类型"),
/*  91 */   REGIST_STATE("regist_state", "registState", "注册国家与地区"),
/*  92 */   IG_FLAG("investment_grade_flag", "investmentGradeFlag", "投资级标识"),
/*  93 */   SCRA_RESULT("scra_result", "scraResult", "标准信用风险评估结果"),
/*     */   
/*  95 */   ISSUER_TYPE("issuer_type", "issuerType", "发行人大类"),
/*  96 */   ISSUER_REGIST_STATE("issuer_regist_state", "issuerRegistState", "发行人注册国家与地区"),
/*  97 */   ISSUER_IG_FLAG("issuer_ig_flag", "issuerIgFlag", "发行人投资级标识"),
/*  98 */   ISSUER_SCRA_RESULT("issuer_scra_result", "issuerScraResult", "发行人标准信用风险评估结果"),
/*     */   
/* 100 */   EXTERNAL_RATING("external_rating", "externalRating", "外部评级"),
/* 101 */   SECU_ISSUE_RATING("secu_issue_rating", "secuIssueRating", "证券发行等级"),
/* 102 */   SECU_ISSUE_MATURITY("secu_issue_maturity", "secuIssueMaturity", "证券剩余期限"),
/* 103 */   FINANCIAL_COLL_RATING("financial_coll_rating", "financialCollRating", "金融质押品发行等级"),
/*     */   
/* 105 */   RW_FI("rw_fi", "rwFi", "金融工具风险权重"),
/*     */   
/* 107 */   EXPOSURE("exposure", "exposure", "风险暴露"),
/* 108 */   RW("rw", "rw", "风险权重"),
/* 109 */   RW_BEFORE("rw_before", "rwBefore", "调整前风险权重"),
/* 110 */   EAD("ead", "ead", "违约风险暴露"),
/* 111 */   EAD_AIRB("ead_airb", "eadAirb", "高级法下违约风险暴露"),
/* 112 */   EAD_IRB("ead_irb", "eadIrb", "内评法下违约风险暴露"),
/* 113 */   CCF_BEFORE("ccf_before", "ccfBefore", "调整前信用转换系数"),
/* 114 */   CCF("ccf", "ccf", "信用转换系数"),
/* 115 */   CCF_IRB("ccf_irb", "ccfIrb", "内评法信用转换系数"),
/* 116 */   CCF_FIRB("ccf_firb", "ccfFirb", "内评初级法信用转换系数"),
/* 117 */   CCF_AIRB("ccf_airb", "ccfAirb", "内评高级法信用转换系数"),
/* 118 */   CCF_AI("ccf_ai", "ccfAi", "内评高级法信用转换系数-映射"),
/*     */   
/* 120 */   ANNUAL_SALE("annual_sale", "annualSale", "公司客户年销售额"),
/* 121 */   ASSET_BALANCE("asset_balance", "assetBalance", "资产余额"),
/* 122 */   PROVISION("provision", "provision", "减值准备"),
/* 123 */   PROVISION_PROP("provision_prop", "provisionProp", "减值准备比例"),
/* 124 */   PROVISION_DED("provision_ded", "provisionDed", "减值准备扣减"),
/* 125 */   REVA_FREQUENCY("reva_frequency", "revaFrequency", "重估频率"),
/* 126 */   MODEL_ID("model_id", "modelId", "内评模型ID"),
/* 127 */   PD_BEFORE("pd_before", "pdBefore", "调整前违约概率"),
/* 128 */   PD("pd", "pd", "违约概率"),
/* 129 */   DEFAULT_LGD("default_lgd", "defaultLgd", "已违约暴露违约损失率"),
/* 130 */   BEEL("beel", "beel", "已违约暴露预期损失比率"),
/*     */   
/* 132 */   PD_LIMIT("pd_limit", "pdLimit", "违约概率下限"),
/*     */   
/* 134 */   LGD_BEFORE("lgd_before", "lgdBefore", "调整前违约损失率"),
/* 135 */   LGD("lgd", "lgd", "违约损失率"),
/* 136 */   LGD_AIRB("lgd_airb", "lgdAirb", "高级法下违约损失率"),
/* 137 */   MATURITY("maturity", "maturity", "有效期限"),
/* 138 */   MATURITY_AIRB("maturity_airb", "maturityAirb", "高级法有效期限"),
/* 139 */   TM("tm", "tm", "风险暴露最低持有期"),
/* 140 */   SH("sh", "sh", "标准折扣系数"),
/* 141 */   SHE("she", "she", "风险暴露标准折扣系数"),
/* 142 */   HE("he", "he", "风险暴露折扣系数"),
/* 143 */   SHC("shc", "shc", "押品标准折扣系数"),
/* 144 */   HC("hc", "hc", "押品折扣系数"),
/* 145 */   LGD_COLL("lgd", "lgd", "抵质押品LGD"),
/*     */   
/* 147 */   S("sa", "sa", "年营业收入"),
/* 148 */   R("rel", "rel", "风险暴露相关性"),
/* 149 */   B("bma", "bma", "期限调整因子"),
/* 150 */   K("kcr", "kcr", "资本要求率"),
/* 151 */   EL("el", "el", "预期损失率"),
/*     */   
/* 153 */   RWA("rwa", "rwa", "rwa"),
/* 154 */   RWA_MA("rwa_ma", "rwaMa", "缓释后RWA"),
/* 155 */   RWA_AA("rwa_aa", "rwaAa", "风险加权资产调整额"),
/* 156 */   RWA_ADJ("rwa_adj", "rwaAdj", "调整后风险加权资产"),
/*     */   
/* 158 */   EC_PARAM_INFO("ec_param_info", "ecParamInfo", "经济资本参数信息"),
/* 159 */   EC_DF("ec_df", "ecDf", "经济资本折扣系数"),
/* 160 */   EC("ec", "ec", "经济资本"),
/*     */   
/* 162 */   QUAL_FLAG_WA("qual_flag_wa", "qualFlagWa", "权重法合格标识"),
/* 163 */   QUAL_FLAG_FIRB("qual_flag_firb", "qualFlagFirb", "内评初级法合格标识"),
/* 164 */   IS_APPLY_WA("is_apply_wa", "isApplyWa", "权重法是否适用"),
/* 165 */   IS_APPLY_FIRB("is_apply_firb", "isApplyFirb", "内评初级法是否适用"),
/*     */   
/* 167 */   MITIGATION_MAIN_TYPE("mitigation_main_type", "mitigationMainType", "缓释工具大类"),
/* 168 */   MITIGATION_SMALL_TYPE("mitigation_small_type", "mitigationSmallType", "缓释工具小类"),
/*     */   
/* 170 */   MITIGATION_RPT_ITEM_WA("mitigation_rpt_item_wa", "mitigationRptItemWa", "缓释工具报表项目"),
/*     */   
/* 172 */   COLLATERAL_AMOUNT("collateral_amount", "collateralAmount", "抵押总额"),
/* 173 */   GUARANTEE_AMOUNT("guarantee_amount", "guaranteeAmount", "保证总额"),
/* 174 */   MITIGATION_AMOUNT("mitigation_amount", "mitigationAmount", "缓释总额"),
/* 175 */   MITIGATION_TYPE("mitigation_type", "mitigationType", "缓释物类型"),
/*     */   
/* 177 */   IS_COVER_DEBT_REST("is_cover_debt_rest", "isCoverDebtRest", "信用事件是否覆盖债务重组"),
/*     */   
/* 179 */   RW_PTY1("rw_pty1", "rwPty1", "优先档次一年期风险权重"),
/* 180 */   RW_PTY5("rw_pty5", "rwPty5", "优先档次五年期风险权重"),
/* 181 */   RW_NPTY1("rw_npty1", "rwNpty1", "非优先档次一年期风险权重"),
/* 182 */   RW_NPTY5("rw_npty5", "rwNpty5", "非优先档次五年期风险权重"),
/*     */   
/* 184 */   OFF_ABS_BIZ_TYPE("off_abs_biz_type", "offAbsBizType", "表外资产证券化业务类型"),
/* 185 */   IS_COMPLIANCE_STC("is_compliance_stc", "isComplianceStc", "是否符合STC标准"),
/* 186 */   TRANCHE_MATURITY("tranche_maturity", "trancheMaturity", "档次期限"),
/* 187 */   TRANCHE_SN("tranche_sn", "trancheSn", "档次级别"),
/* 188 */   TRANCHE_LEVEL("tranche_level", "trancheLevel", "档次级别"),
/* 189 */   T("thickness", "thickness", "档次厚度"),
/* 190 */   NPT_RW_AC("npt_rw_ac", "nptRwAc", "非优先档次风险权重调整系数"),
/* 191 */   TRANCHE_STARTING_POINT("tranche_starting_point", "trancheStartingPoint", "档次起始点"),
/* 192 */   TRANCHE_SEPARATION_POINT("tranche_separation_point", "trancheSeparationPoint", "档次分离点"),
/* 193 */   QUAL_FACILITY_FLAG("qual_facility_flag", "qualFacilityFlag", "合格便利标识"),
/* 194 */   ABS_UA_FLAG("abs_ua_flag", "absUaFlag", "资产证券化基础资产标识"),
/* 195 */   IS_ORIGINATOR("is_originator", "isOriginator", "是否发起机构"),
/* 196 */   IS_COMP_REQU("is_comp_requ", "isCompRequ", "是否符合监管要求"),
/* 197 */   UNDER_ASSET_TYPE("under_asset_type", "underAssetType", "基础资产类型"),
/* 198 */   NP_ASSET_FLAG("np_asset_flag", "npAssetFlag", "不良资产标识"),
/* 199 */   IS_IRB_CALC("is_irb_calc", "isIrbCalc", "是否使用内评法计算"),
/* 200 */   IS_NFS_REFLECT_RATING("is_nfs_reflect_rating", "isNfsReflectRating", "是否提供非资金支持并反映到外部评级"),
/* 201 */   IS_APPROVED_RATING("is_approved_rating", "isApprovedRating", "是否认可外部评级"),
/*     */   
/* 203 */   RETURN_FLAG("flag", "flag", "脚本执行结果"),
/*     */   
/* 205 */   CURRENCY("currency", "currency", "币种"),
/* 206 */   AS_CURRENCY_FLAG("as_currency_flag", "asCurrencyFlag", "记账本位币标识"),
/*     */   
/* 208 */   AP_IRB_PROP("ap_irb_prop", "apirbProp", "资产池内评法计算比重"),
/* 209 */   AP_AB("ap_ab", "apAb", "资产池资产余额"),
/* 210 */   AP_EAD("ap_ead", "apEad", "资产池风险暴露"),
/* 211 */   AP_RWA("ap_rwa", "apRwa", "资产池风险加权资产"),
/* 212 */   AP_LGD("ap_lgd", "apLgd", "资产池加权平均违约损失率"),
/* 213 */   AP_EN("ap_en", "apEn", "资产池风险暴露有效数量"),
/* 214 */   IS_DECENTRALIZED("is_decentralized", "isDecentralized", "资产池是否分散"),
/* 215 */   AP_RETAIL_FLAG("ap_retail_flag", "apRetailFlag", "资产池零售标识"),
/*     */   
/* 217 */   IRB_EAD_TOTAL("irb_ead_total", "irbEadTotal", "资产池内评法风险暴露汇总"),
/* 218 */   IRB_EAD2_TOTAL("irb_ead2_total", "irbEad2Total", "资产池内评法风险暴露平方的汇总"),
/* 219 */   IRB_LGD_EAD_TOTAL("irb_lgd_ead_total", "irbLgdEadTotal", "资产池内评法违约损失率加权违约风险暴露"),
/*     */   
/* 221 */   AP_ODE_AB("ap_ode_ab", "apOdeAb", "资产池已逾期风险暴露资产余额"),
/* 222 */   AP_ODE_EAD("ap_ode_ead", "apOdeEad", "资产池已逾期暴露违约风险暴露"),
/* 223 */   AP_UNKE_AB("ap_unke_ab", "apUnkeAb", "资产池未知风险暴露资产余额"),
/* 224 */   AP_UNKE_EAD("ap_unke_ead", "apUnkeEad", "资产池未知暴露违约风险暴露"),
/*     */   
/* 226 */   AP_AIRB_PROP("ap_airb_prop", "apAirbProp", "资产池高级法计算比重"),
/* 227 */   AP_AIRB_AB("ap_airb_ab", "apAirbAb", "资产池高级法资产余额"),
/* 228 */   AP_AIRB_EAD("ap_airb_ead", "apAirbEad", "资产池高级法风险暴露"),
/* 229 */   AP_AIRB_RWA("ap_airb_rwa", "apAirbRwa", "资产池高级法风险加权资产"),
/* 230 */   AP_AIRB_ELA("ap_airb_ela", "apAirbEla", "资产池高级法预期损失额"),
/* 231 */   AP_AIRB_PROV("ap_airb_prov", "apAirbProv", "资产池高级法准备金"),
/*     */   
/* 233 */   AP_FIRB_PROP("ap_firb_prop", "apFirbProp", "资产池初级法计算比重"),
/* 234 */   AP_FIRB_AB("ap_firb_ab", "apFirbAb", "资产池初级法资产余额"),
/* 235 */   AP_FIRB_EAD("ap_firb_ead", "apFirbEad", "资产池初级法风险暴露"),
/* 236 */   AP_FIRB_RWA("ap_firb_rwa", "apFirbRwa", "资产池初级法风险加权资产"),
/* 237 */   AP_FIRB_ELA("ap_firb_ela", "apFirbEla", "资产池初级法预期损失额"),
/* 238 */   AP_FIRB_PROV("ap_firb_prov", "apFirbProv", "资产池初级法准备金"),
/*     */   
/* 240 */   AP_WA_PROP("ap_wa_prop", "apWaProp", "资产池权重法计算比重"),
/* 241 */   AP_WA_AB("ap_wa_ab", "apWaAb", "资产池权重法资产余额"),
/* 242 */   AP_WA_EAD("ap_wa_ead", "apWaEad", "资产池权重法风险暴露"),
/* 243 */   AP_WA_RWA("ap_wa_rwa", "apWaRwa", "资产池权重法风险加权资产"),
/* 244 */   AP_WA_MAX_RW("ap_wa_max_rw", "apWaMaxRw", "资产池权重法最高风险权重"),
/* 245 */   AP_WA_ARW("ap_wa_arw", "apWaArw", "资产池权重法平均风险权重"),
/* 246 */   AP_WARW("ap_warw", "apWarw", "资产池平均风险权重"),
/* 247 */   AP_TYPE("ap_type", "apType", "资产池类型"),
/*     */   
/* 249 */   AP_KIRB("ap_kirb", "apKirb", "资产池内部评级法监管资本要求"),
/* 250 */   AP_KSA("ap_ksa", "apKsa", "资产池权重法监管资本要求"),
/* 251 */   AP_KA("ap_ka", "apKa", "资产池调整后监管资本要求"),
/* 252 */   KSSFA("kssfa", "kssfa", "单位资产证券化风险暴露的资本要求"),
/*     */   
/* 254 */   SFA("sfa", "sfa", "监管因子A"),
/* 255 */   SFB("sfb", "sfb", "监管因子B"),
/* 256 */   SFC("sfc", "sfc", "监管因子C"),
/* 257 */   SFD("sfd", "sfd", "监管因子D"),
/* 258 */   SFE("sfe", "sfe", "监管因子E"),
/* 259 */   SFP("sfp", "sfp", "监管因子P"),
/* 260 */   VARA("vara", "vara", "变量a"),
/* 261 */   VARU("varu", "varu", "变量u"),
/* 262 */   VARL("varl", "varl", "变量l"),
/*     */   
/* 264 */   TRANCHE_PROP("tranche_prop", "trancheProp", "档次持有比例"),
/* 265 */   MAX_TRANCHE_PROP("max_tranche_prop", "maxTrancheProp", "档次最高持有比例"),
/* 266 */   PRODUCT_RWA("product_rwa", "productRwa", "产品风险加权资产"),
/* 267 */   PRODUCT_RWA_LIMIT("product_rwa_limit", "productRwaLimit", "产品风险加权资产上限"),
/* 268 */   PRODUCT_RWA_AA("product_rwa_aa", "productRwaAa", "产品风险加权资产调整额"),
/* 269 */   SECURITIES_CODE("securities_code", "securitiesCode", "证券代码"),
/*     */   
/* 271 */   MARGIN_TRADING_FLAG("margin_trading_flag", "marginTradingFlag", "保证金交易标识"),
/* 272 */   MARGIN_RISK_PERIOD("margin_risk_period", "marginRiskPeriod", "保证金风险期间"),
/*     */   
/* 274 */   CENTRAL_CLEAR_FLAG("central_clear_flag", "centralClearFlag", "中央清算标识"),
/* 275 */   TRADING_ROLE("trading_role", "tradingRole", "交易角色"),
/*     */   
/* 277 */   IS_OUR_BANK_SUBMIT("is_our_bank_submit", "isOurBankSubmit", "是否我行提交"),
/* 278 */   BANKRUPTCY_SEPARATION_FLAG("bankruptcy_separation_flag", "bankruptcySeparationFlag", "破产分离标识"),
/*     */   
/* 280 */   NOTIONAL_PRINCIPAL("notional_principal", "notionalPrincipal", "名义本金"),
/* 281 */   MWP("mwp", "mwp", "有效期限加权名义本金"),
/* 282 */   CP_MATURITY("cp_maturity", "cpMaturity", "交易对手有效期限"),
/* 283 */   DISCOUNT_FACTOR("discount_factor", "discountFactor", "监管折现因子"),
/* 284 */   CP_RW("cp_rw", "cpRw", "交易对手风险权重"),
/* 285 */   AS_EAD("as_ead", "asEad", "监管调整后违约风险暴露"),
/* 286 */   SCVA("scva", "scva", "交易对手信用估值调整"),
/*     */   
/* 288 */   FACTOR_LINE("factor_line", "factorLine", "折扣系数底线"),
/*     */   
/* 290 */   QUAL_CCP_FLAG("qual_ccp_flag", "qualCcpFlag", "合格中央交易对手标识"),
/*     */   
/* 292 */   DF_PREPAID("df_prepaid", "dfPrepaid", "预付违约基金"),
/* 293 */   DF_UNPAID("df_unpaid", "dfUnpaid", "未付违约基金"),
/* 294 */   K_CCD("k_ccd", "kCcd", "中央交易对手应计提虚拟资本要求"),
/* 295 */   DF_CM("df_cm", "dfCm", "清算会员预缴资源总额"),
/* 296 */   DF_CCD("df_ccd", "dfCcd", "中央交易对手自身财务资源"),
/* 297 */   DF("df", "df", "违约基金"),
/* 298 */   RC("rc", "rc", "资本要求"),
/*     */   
/* 300 */   SUPERVISION_FACTOR("supervision_factor", "supervisionFactor", "监管因子"),
/* 301 */   SUPERVISION_COEFFICIENT("supervision_coefficient", "supervisionCoefficient", "监管相关系数"),
/* 302 */   SUPERVISION_VOLATILITY("supervision_volatility", "supervisionVolatility", "监管波动率"),
/*     */   
/* 304 */   ADDON_FACTOR("addon_factor", "addonFactor", "Add-on附加系数"),
/* 305 */   EAD_HAIRCUT("ead_haircut", "eadHaircut", "风险暴露折扣系数");
/*     */   
/*     */   private String code;
/*     */   
/*     */   private String field;
/*     */   
/*     */   private String name;
/*     */   
/*     */   RwaParam(String code, String field, String name) {
/* 314 */     this.code = code;
/* 315 */     this.field = field;
/* 316 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 321 */     return this.code;
/*     */   }
/*     */   
/*     */   public String getField() {
/* 325 */     return this.field;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 329 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\RwaParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */