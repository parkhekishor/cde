package org.cde.cdephone.service;

import java.util.List;

import org.cde.domain.entity.EntityPhone;

public interface PhoneStandardizationService {

	void standardize(List<EntityPhone> entityPhones);

}
