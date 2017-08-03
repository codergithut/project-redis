package com.common.model;

import java.util.Map;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
public class RabbitmqQueueInfo {
    private String name;

    private String vhost;

    private String durable;

    private String node;

    private MessageStatus message_stats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getDurable() {
        return durable;
    }

    public void setDurable(String durable) {
        this.durable = durable;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setMessage_stats(MessageStatus message_stats) {
        this.message_stats = message_stats;
    }

    private class MessageStatus {

        private Integer publish;

        private Integer deliver_get;

        public Integer getPublish() {
            return publish;
        }

        public void setPublish(Integer publish) {
            this.publish = publish;
        }

        public Integer getDeliver_get() {
            return deliver_get;
        }

        public void setDeliver_get(Integer deliver_get) {
            this.deliver_get = deliver_get;
        }
    }
}
