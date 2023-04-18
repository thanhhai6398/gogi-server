package vn.com.gigo.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import vn.com.gigo.exception.ResourceNotFoundException;

@Service
public class OrderNotificaion implements Sender {
	public static final Map<Long, List<SseEmitter>> emitters = new HashMap<>();
	private static Map<Long,Integer> listViewer = new HashMap<>();
	private final String EVENT_NAME = "LIST_ORDERS_UPDATE";
	

	public void setNotification(Notification notification) {
		this.sendNotification(notification);
	}
	
	public List<SseEmitter> getListEmittersByStoreId(Long storeId){
		return emitters.get(storeId);
	}
		
	private void removeEmitter(Long storeId,Long employeeId) {
		Integer index = listViewer.get(employeeId);
		if(index != null) {
			emitters.get(storeId).remove((int)index);
		}
		else throw new ResourceNotFoundException("Client with does not exist");
	}
	
	private boolean checkClientExist(Long employeeId) {
		return listViewer.containsKey(employeeId);
	}
	public SseEmitter addNewEmitter(Long storeId, Long employeeId) {
		if(emitters.get(storeId) == null) {
			List<SseEmitter> emittersForStore = new ArrayList<>();
			emitters.put(storeId, emittersForStore);
		}
		if(checkClientExist(employeeId)) {
			Integer index = listViewer.get(employeeId);
			return emitters.get(storeId).get(index);
		}else {
			SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
			sendEvent(sseEmitter, "Init connection");
			sseEmitter.onCompletion(() -> removeEmitter(storeId,employeeId));
			sseEmitter.onTimeout(() ->  removeEmitter(storeId,employeeId));
			sseEmitter.onError((e) -> {
				e.printStackTrace();
				removeEmitter(storeId,employeeId);
			});
			emitters.get(storeId).add(sseEmitter);
			listViewer.put(employeeId,emitters.get(storeId).size() - 1);
			return sseEmitter;
		}
		
	}

	public void sendEvent(SseEmitter emitter, Object data) {
		if (emitter == null) {
			throw new ResourceNotFoundException("Not found emmitter");
		}
		try {
			emitter.send(SseEmitter.event().name(EVENT_NAME).data(data, MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendNotification(Notification notification) {
		List<SseEmitter> listReceiver = emitters.get(notification.getReceiverId());
		for(int i=listReceiver.size() - 1; i >= 0; i--) {
			sendEvent(listReceiver.get(i), notification.getContent());
		}
	}
}
