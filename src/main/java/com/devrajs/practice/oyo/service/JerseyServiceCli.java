package com.devrajs.practice.oyo.service;

import com.inmobi.indis.recipes.JerseyNettyServiceRecipe;
import com.inmobi.indis.server.Service;


/**
 * Created by devraj.singh on 12/13/15.
 */
public class JerseyServiceCli  {

    public static void main(String[] args) throws Exception {
        Service service = new JerseyNettyServiceRecipe().cook().build();
        service.getLifecycle().start();
    }
}
