package com.easy.sdk.common.config.auth.core;

public abstract class AuthUserDTO implements IAuthUser {

	protected String className;
	protected Integer userType;

	@Override
	public String getClassName() {
		return this.getClass().getName();
	}

}
