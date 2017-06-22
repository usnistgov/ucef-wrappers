// This code has been generated by the C2W code generator.
// Do not edit manually!

package GLDTestFederation;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

import c2w.hla.C2WInteractionRoot;
import c2w.hla.C2WLogger;
import c2w.hla.InteractionRoot;
import c2w.hla.SubscribedInteractionFilter;
import c2w.hla.SynchronizedFederate;


import c2w.hla.*;

public class TestFederateBase extends SynchronizedFederate {

	private SubscribedInteractionFilter _subscribedInteractionFilter = new SubscribedInteractionFilter();
	
	// constructor
	public TestFederateBase( String federation_id, String federate_id ) throws Exception {
	
		setLookahead( 0.2 );
		createRTI();
		joinFederation( federation_id, federate_id );

		enableTimeConstrained();

		enableTimeRegulation( getLookahead() );
		enableAsynchronousDelivery();
        // interaction pubsub
        
        F1_house_A15.publish( getRTI() );
        SimTime.publish( getRTI() );
        
        F1_house_A15.subscribe( getRTI() );
        _subscribedInteractionFilter.setFedFilters( 
			F1_house_A15.get_handle(), 
			SubscribedInteractionFilter.OriginFedFilter.ORIGIN_FILTER_DISABLED, 
			SubscribedInteractionFilter.SourceFedFilter.SOURCE_FILTER_DISABLED 
		);
        F1_house_A11.subscribe( getRTI() );
        _subscribedInteractionFilter.setFedFilters( 
			F1_house_A11.get_handle(), 
			SubscribedInteractionFilter.OriginFedFilter.ORIGIN_FILTER_DISABLED, 
			SubscribedInteractionFilter.SourceFedFilter.SOURCE_FILTER_DISABLED 
		);		
		// object pubsub
        
        	
        F1_house_A8.publish_number_of_stories();
        F1_house_A8.publish( getRTI() );
                
        	
        F1_house_A8.subscribe_number_of_stories();
        F1_house_A8.subscribe( getRTI() );
        
        	
        market.subscribe_market_id();
        market.subscribe_period();
        market.subscribe_unit();
        market.subscribe( getRTI() );
                }
        
       // constructor
	public TestFederateBase(  String[] federationInfo ) throws Exception {

		setLookahead( 0.2 );
		createRTI();
		joinFederation( federationInfo[ 0 ], federationInfo[ 1 ] );

		String loglevel = null;
		/*if(federationInfo.length == 3)
			C2WLogger.init( federationInfo[ 2 ] );
		else if(federationInfo.length > 3)
			C2WLogger.init( federationInfo[ 2 ], federationInfo[ 3 ] );		
		
		if(federationInfo.length == 5)
			loglevel = federationInfo[ 4 ];*/

		enableTimeConstrained();
		enableTimeRegulation( getLookahead() );
		enableAsynchronousDelivery();

        // interaction pubsub

        F1_house_A15.publish( getRTI() );
        SimTime.publish( getRTI() );

        F1_house_A15.subscribe( getRTI() );
        _subscribedInteractionFilter.setFedFilters( 
			F1_house_A15.get_handle(), 
			SubscribedInteractionFilter.OriginFedFilter.ORIGIN_FILTER_DISABLED, 
			SubscribedInteractionFilter.SourceFedFilter.SOURCE_FILTER_DISABLED 
		);
        F1_house_A11.subscribe( getRTI() );
        _subscribedInteractionFilter.setFedFilters( 
			F1_house_A11.get_handle(), 
			SubscribedInteractionFilter.OriginFedFilter.ORIGIN_FILTER_DISABLED, 
			SubscribedInteractionFilter.SourceFedFilter.SOURCE_FILTER_DISABLED 
		);		// object pubsub
        
        	
        F1_house_A8.publish_number_of_stories();
        F1_house_A8.publish( getRTI() );
                
        	
        F1_house_A8.subscribe_number_of_stories();
        F1_house_A8.subscribe( getRTI() );
        
        	
        market.subscribe_market_id();
        market.subscribe_period();
        market.subscribe_unit();
        market.subscribe( getRTI() );
        		// enable pubsub log
		if(federationInfo.length  > 2) {
			
			F1_house_A15.enablePublishLog(
				"F1_house_A15",
				"TestFederate",
				"NORMAL",
				loglevel);
			SimTime.enablePublishLog(
				"SimTime",
				"TestFederate",
				"NORMAL",
				loglevel);
			
			F1_house_A15.enableSubscribeLog(
				"F1_house_A15",
				"TestFederate", 
				"NORMAL", 
				loglevel);
			F1_house_A11.enableSubscribeLog(
				"F1_house_A11",
				"TestFederate", 
				"NORMAL", 
				loglevel);	
			
	        	
	        F1_house_A8.enablePublishLog(
	        	"F1_house_A8",	
	        	"number_of_stories",
	        	"TestFederate",
	        	"NORMAL",
	        	loglevel);
			
        		
	        	F1_house_A8.enableSubscribeLog(
	        	"F1_house_A8",	
	        	"number_of_stories",
	        	"TestFederate",
	        	"NORMAL",
	        	loglevel);
        		
	        	market.enableSubscribeLog(
	        	"market",	
	        	"market_id",
	        	"TestFederate",
	        	"NORMAL",
	        	loglevel);
	        	market.enableSubscribeLog(
	        	"market",	
	        	"period",
	        	"TestFederate",
	        	"NORMAL",
	        	loglevel);
	        	market.enableSubscribeLog(
	        	"market",	
	        	"unit",
	        	"TestFederate",
	        	"NORMAL",
	        	loglevel);
		}
		
	}
	
	public F1_house_A15 create_F1_house_A15() {
	   F1_house_A15 interaction = new F1_house_A15();
	   interaction.set_sourceFed( getFederateId() );
	   interaction.set_originFed( getFederateId() );
	   return interaction;
	}
	public SimTime create_SimTime() {
	   SimTime interaction = new SimTime();
	   interaction.set_sourceFed( getFederateId() );
	   interaction.set_originFed( getFederateId() );
	   return interaction;
	}
	@Override
	public void receiveInteraction(
	 int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag
	) {
		InteractionRoot interactionRoot = InteractionRoot.create_interaction( interactionClass, theInteraction );
		if ( interactionRoot instanceof C2WInteractionRoot ) {
			
			C2WInteractionRoot c2wInteractionRoot = (C2WInteractionRoot)interactionRoot;

	        // Filter interaction if src/origin fed requirements (if any) are not met
	        if (  _subscribedInteractionFilter.filterC2WInteraction( getFederateId(), c2wInteractionRoot )  ) {
	        	return;
	        } 
		}
		
		super.receiveInteraction( interactionClass, theInteraction, userSuppliedTag );			
	}

	@Override
	public void receiveInteraction(
	 int interactionClass,
	 ReceivedInteraction theInteraction,
	 byte[] userSuppliedTag,
	 LogicalTime theTime,
	 EventRetractionHandle retractionHandle
	) {
		InteractionRoot interactionRoot = InteractionRoot.create_interaction( interactionClass, theInteraction, theTime );
		if ( interactionRoot instanceof C2WInteractionRoot ) {

			C2WInteractionRoot c2wInteractionRoot = (C2WInteractionRoot)interactionRoot;

	        // Filter interaction if src/origin fed requirements (if any) are not met
	        if (  _subscribedInteractionFilter.filterC2WInteraction( getFederateId(), c2wInteractionRoot )  ) {
	        	return;
	        } 
		}

		super.receiveInteraction( interactionClass, theInteraction, userSuppliedTag, theTime, retractionHandle );			
	}
}
