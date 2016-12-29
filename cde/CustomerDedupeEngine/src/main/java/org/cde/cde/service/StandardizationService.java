package org.cde.cde.service;

import java.util.List;

import org.cde.cdeaddress.AddressStandardizationService;
import org.cde.cdeaddress.in.impl.AddressStandardizationServiceImpl;
import org.cde.cdeemail.EmailStandardizationService;
import org.cde.cdeemail.service.DOBStandardizationService;
import org.cde.cdeemail.service.in.impl.DOBStandardizationServiceImpl;
import org.cde.cdeemail.service.in.impl.EmailStandardizationServiceImpl;
import org.cde.cdeids.service.IdsStandardizationService;
import org.cde.cdeids.service.in.impl.IdsStandardizationServiceImpl;
import org.cde.cdename.service.NameStandardizationService;
import org.cde.cdename.service.in.impl.NameStandardizationServiceImpl;
import org.cde.cdephone.service.PhoneStandardizationService;
import org.cde.cdephone.service.in.impl.PhoneStandardizationServiceImpl;
import org.cde.domain.entity.Entity;
import org.cde.domain.entity.EntityAddress;
import org.cde.domain.entity.EntityDob;
import org.cde.domain.entity.EntityEmail;
import org.cde.domain.entity.EntityId;
import org.cde.domain.entity.EntityName;
import org.cde.domain.entity.EntityPhone;

public class StandardizationService {
	NameStandardizationService nameService_ = new NameStandardizationServiceImpl();
	AddressStandardizationService addressService_ = new AddressStandardizationServiceImpl();
	EmailStandardizationService emailService_ = new EmailStandardizationServiceImpl();
	IdsStandardizationService idsService_ = new IdsStandardizationServiceImpl();
	PhoneStandardizationService phoneService_ = new PhoneStandardizationServiceImpl();
	DOBStandardizationService dobService_ = new DOBStandardizationServiceImpl();

	public void standardize(List<Entity> entities, Long bacthId) {
		for (Entity entity : entities) {
			standardize(entity, bacthId);
		}
	}

	public void standardize(Entity entity, Long bacthId) {

		List<EntityAddress> addresses = entity.getAddresses();
		if (addresses != null && addresses.size() > 0)
			addressService_.standardize(addresses);

		List<EntityName> names = entity.getNames();
		if (names != null && names.size() > 0)
			nameService_.standardize(names);

		List<EntityEmail> emails = entity.getEmails();
		if (emails != null && emails.size() > 0)
			emailService_.standardize(emails);

		List<EntityId> ids = entity.getIds();
		if (ids != null && ids.size() > 0)
			idsService_.standardize(ids);

		List<EntityPhone> phones = entity.getPhones();
		if (phones != null && phones.size() > 0)
			phoneService_.standardize(phones);

		EntityDob dob = entity.getDob();
		if (dob != null)
			dobService_.standardize(dob);

	}

}
