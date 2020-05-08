package drama.painter.realtime.delivery.service.inf;

import drama.painter.realtime.delivery.model.ChatPO;

/**
 * @author murphy
 */
public interface IChat {
    /**
     * 保存聊天记录
     *
     * @param chatpo
     * @param index
     */
    void save(ChatPO chatpo, String index);
}
