package com.easy.sdk.common.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ServerEndpoint("/ws/health")
public class HealthWebSocket extends BaseWebSocket<String> {

	@OnOpen
	public void onOpen(Session session) {
		sid = session.getId();
		//
		boolean exist = super.exist(sid);
		if (exist) {// 关闭连接
			this.alreadyConn(session);
			return;
		}
		// 连接websocket
		super.open(session, sid);
	}

	/**
	 * 发送消息
	 */
	public void sendMsg(String sid, WsMsgDTO msg) {
		BaseWebSocket<String> socket = webSocketMap.get(sid);
		if (socket != null) {
			socket.sendMessage(msg);
		}
	}

	/**
	 * 是否已经连接
	 */
	public boolean contains(String sid) {
		return webSocketMap.containsKey(sid);
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@Override
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口" + sid + "的信息:" + message);
		// 处理连接

	}

	// 已经连接
	private void alreadyConn(Session session) {
		try {
			session.getBasicRemote().sendText(JSONUtil.toJsonStr(WsMsgDTO.buildMsg(MsgEnum.CLOSE, "您已经在另一地方连接！")));
			if (session.isOpen()) {
				session.close();
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	@Override
	@OnClose
	public void onClose() {
		super.onClose();
	}

	@Override
	@OnError
	public void onError(Session session, Throwable error) {
		super.onError(session, error);
	}

	/**
	 * 下线
	 * 
	 */
	public void offline(String sid) {
		try {
			BaseWebSocket<String> socket = webSocketMap.get(sid);
			if (socket == null) {
				throw new ValidateException("您已经下线");
			}
			this.alreadyConn(socket.getSession());
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * 给全体发送消息
	 * 
	 */
	public void notifyToAll() {
		if (CollUtil.isEmpty(webSocketMap)) {
			return;
		}
		HealthCoreModel model = HealthConstant.buildHealthCoreModel();
		// 异步发送消息
		CollUtil.forEach(webSocketMap.values(), (ws, i) -> {
			try {
				ws.sendAsyncMessage(WsMsgDTO.buildMsg(MsgEnum.RECEIVE, model));
			} catch (Exception e) {
				log.error("【{}-{}】: 消息发送失败: {}", ws.getSid(), i, e.getMessage());
			}
		});

	}

	@Override
	public boolean isExit() {
		return false;
	}

}
