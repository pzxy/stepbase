package com.base.core.head.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author start 
 */
public enum TaskTypeEnum {

    /**
     * 事件开始
     */
	@Schema(title="事件开始")
    START_EVENT,
    /**
     * 事件结束
     */
	@Schema(title="事件结束")
    END_EVENT,
    /**
     * 用户任务
     */
	@Schema(title="用户任务")
    USER_TASK,
    /**
     * 网关分支
     */
	@Schema(title="网关分支")
    EXCLUSIVE_GATEWAY,
    /**
     * 抄送
     */
	@Schema(title="抄送")
    CC

}
