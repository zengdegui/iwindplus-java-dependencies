package com.iwindplus.boot.pay.domain.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 支付渠道枚举.
 *
 * @author zengdegui
 * @since 2018年10月11日
 */
@Getter
@Accessors(fluent = true)
public enum PayChannelEnum {
	ALIPAY("alipay", "支付宝"),
	WECHAT_PAY("wechat_pay", "微信"),
	CREDIT_CARD_PAY("credit_card_pay", "信用卡"),
	BANK_CARD_PAY("bank_card_pay", "银行卡"),
	;

	@EnumValue
	private final String value;

	private final String desc;

	private PayChannelEnum(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static PayChannelEnum valueOfValue(String value) {
		for (PayChannelEnum val : PayChannelEnum.values()) {
			if (Objects.equals(value, val.value())) {
				return val;
			}
		}
		return null;
	}
}