/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren;

import io.renren.modules.service.controller.realdata.SRealDataTask;
import io.renren.modules.service.controller.websocket.AccountRealData;
import io.renren.modules.service.controller.websocket.HomeRealData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class RenrenApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(RenrenApplication.class);
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		AccountRealData.setApplicationContext(configurableApplicationContext);
		HomeRealData.setApplicationContext(configurableApplicationContext);
		SRealDataTask.setApplicationContext(configurableApplicationContext);
	}

}