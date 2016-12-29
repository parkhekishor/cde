package org.cde.cdeemail;

import java.util.List;

import org.cde.domain.entity.EntityEmail;

public interface EmailStandardizationService {

	public void standardize(List<EntityEmail> entityEmails);

}
