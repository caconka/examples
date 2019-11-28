import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RxLoop {

	public Single<List<Communication>> getCommunications(String xCorrelation, String from, String to, String msisdns) {
		final String[] msisdnList = msisdns.split(",");

		Observable<Single<List<Communication>>> emmiterOpen = Observable.create(subscriber -> {
			for (String msisdn : msisdnList) {
				subscriber.onNext(
						communicationsDao.getCommunicationsByMsisdn(xCorrelation, from, to, msisdn) // Returns Single<List<Communication>>
							.onErrorResumeNext(throwable -> {
								LOGGER.warn(throwable.getMessage());
								final EventError eventError =
									new EventError("", msisdn, ERROR_MESSAGE + msisdn, throwable.getMessage());
								final List<Communication> communications = new ArrayList<Communication>();
								communications.add(new Communication(eventError));
								return Single.just(communications);
							}));
			}
			subscriber.onComplete();
		});

		return emmiterOpen.flatMapSingle(o -> o).toList()
			.map(communicationLists -> getCommunicationsFromLists(communicationLists));
	}

	private List<Communication> getCommunicationsFromLists(List<List<Communication>> communicationLists) {
		return communicationLists.stream()
			.flatMap(List::stream)
			.collect(Collectors.toList());
	}
}
