package com.qdm.cg.clients.service;

import java.util.List;

import com.qdm.cg.clients.dto.SubscriptionsDTO;
import com.qdm.cg.clients.entity.Subscriptions;

public interface SubscriptionsService {

	Subscriptions addSubscriptions(SubscriptionsDTO subscriptionsDTO);

	List<Subscriptions> getSubscriptions();

}
