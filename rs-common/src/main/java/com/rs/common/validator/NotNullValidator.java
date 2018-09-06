package com.rs.common.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.rs.common.base.BaseResultConstant;

/**
 * 校验不为null
 * @author liegou
 */
public class NotNullValidator extends ValidatorHandler<String> implements Validator<String> {

    private String fieldName;

    public NotNullValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean validate(ValidatorContext context, String s) {
        if (null == s) {
            context.addError(ValidationError.create(String.format("%s不能为空！", fieldName))
                    .setErrorCode(BaseResultConstant.FAILED.getCode())
                    .setField(fieldName)
                    .setInvalidValue(s));
            return false;
        }
        return true;
    }

}
