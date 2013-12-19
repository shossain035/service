package com.lithouse.common.dao.impl;

import java.util.Map;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.GetIdentityVerificationAttributesRequest;
import com.amazonaws.services.simpleemail.model.IdentityVerificationAttributes;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityRequest;
import com.lithouse.common.dao.DeveloperDao;
import com.lithouse.common.model.DeveloperItem;
import com.lithouse.common.util.Global;

public class DeveloperDaoImpl extends GenericDaoImpl implements DeveloperDao {

	@Override
	public DeveloperItem findWithIFTTTVerification ( String developerId ) {
		if ( developerId == null || developerId.isEmpty ( ) ) {
			return null;
		}
		
		DeveloperItem developer = find ( DeveloperItem.class, developerId );
		
		if ( null == developer ) {
			return null;
		}
		
		return updateIFTTTActivationStatus ( developer );
	}
	
	private DeveloperItem updateIFTTTActivationStatus ( DeveloperItem developer ) {
		if ( developer.getIFTTTEmailAddress ( ) == null
				|| DeveloperItem.ActivationStatus.SUCCESS.equalsIgnoreCase ( developer.getIFTTTActivationStatus ( ) ) ) {
			return developer;
		}
		
		try {
			AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient ( Global.getAwsCredentials ( ) );

			Map < String, IdentityVerificationAttributes > map  = 
				ses.getIdentityVerificationAttributes ( 
						new GetIdentityVerificationAttributesRequest ( ).withIdentities ( developer.getIFTTTEmailAddress ( ) ) )
								.getVerificationAttributes ( );
		
			String verificationStatus = map.get ( developer.getIFTTTEmailAddress ( ) ).getVerificationStatus ( );
			
			if ( !verificationStatus.equalsIgnoreCase ( developer.getIFTTTActivationStatus ( ) ) ) {
				developer.setIFTTTActivationStatus ( verificationStatus );
				save ( developer );
			}
			
		} catch ( Exception e ) {
			Global.getLogger ( ).error ( "Error while IFTTT email verification", e );
		}
		return developer;
	}
	
	@Override
	public DeveloperItem updateDeveloper ( DeveloperItem developerItem ) {
		DeveloperItem savedDeveloper = find ( DeveloperItem.class, developerItem.getDeveloperId ( ) );
		
		if ( savedDeveloper == null ) return null;
		
		// TODO: Consider other attributes			
		if ( developerItem.getIFTTTEmailAddress ( ) != null && !developerItem.getIFTTTEmailAddress ( ).isEmpty ( )) {
			savedDeveloper.setIFTTTActivationStatus ( DeveloperItem.ActivationStatus.PENDING );
			savedDeveloper.setIFTTTEmailAddress ( developerItem.getIFTTTEmailAddress ( ) );
			
			save ( savedDeveloper );
			
			AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient ( Global.getAwsCredentials ( ) );
			ses.verifyEmailIdentity ( new VerifyEmailIdentityRequest()
								.withEmailAddress ( developerItem.getIFTTTEmailAddress ( ) ) );
		}
		
		
		return savedDeveloper;
	}

}
