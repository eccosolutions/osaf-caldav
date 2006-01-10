/*
 * Copyright 2005 Open Source Applications Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osaf.caldav4j;

import java.io.InputStream;

import junit.framework.TestCase;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.methods.DeleteMethod;
import org.osaf.caldav4j.methods.CalDAV4JMethodFactory;
import org.osaf.caldav4j.methods.MkCalendarMethod;
import org.osaf.caldav4j.methods.PutMethod;


/**
 * Base class for CalDAV4j tests.
 */
public abstract class BaseTestCase
    extends TestCase{
    private static final Log log = LogFactory.getLog(BaseTestCase.class);
    private HttpClient http = createHttpClient();
    private HostConfiguration hostConfig = createHostConfiguration();

    public static final String CALDAV_SERVER_HOST = "localhost";
    public static final int CALDAV_SERVER_PORT = 8080;
    public static final String CALDAV_SERVER_PROTOCOL = "http";
    public static final String CALDAV_SERVER_WEBDAV_ROOT = "/home/test/";
    public static final String CALDAV_SERVER_USERNAME = "test";
    public static final String CALDAV_SERVER_PASSWORD = "password";

    public static final String ICS_DAILY_NY_5PM = "Daily_NY_5pm.ics";
    public static final String ICS_DAILY_NY_5PM_UID = "DE916949-731D-4DAE-BA93-48A38B2B2030";
    public static final String ICS_DAILY_NY_5PM_SUMMARY = "Daily_NY_5pm";

    public static final String ICS_ALL_DAY_JAN1 = "All_Day_NY_JAN1.ics";
    public static final String ICS_NORMAL_PACIFIC_1PM = "Normal_Pacific_1pm.ics";
    public static final String ICS_SINGLE_EVENT= "singleEvent.ics";

    private CalDAV4JMethodFactory methodFactory = new CalDAV4JMethodFactory();
    
    public String getCalDAVServerHost() {
        return CALDAV_SERVER_HOST;
    }
    
    public int getCalDAVServerPort(){
        return CALDAV_SERVER_PORT;
    }
    
    public String getCalDavSeverProtocol(){
        return CALDAV_SERVER_PROTOCOL;
    }
    
    public String getCalDavSeverWebDAVRoot(){
        return CALDAV_SERVER_WEBDAV_ROOT;
    }
    
    public String getCalDavSeverUsername(){
        return CALDAV_SERVER_USERNAME;
    }
    
    public String getCalDavSeverPassword(){
        return CALDAV_SERVER_PASSWORD;
    }
    
    public HttpClient createHttpClient(){

        HttpClient http = new HttpClient();

        Credentials credentials = new UsernamePasswordCredentials(CALDAV_SERVER_USERNAME, CALDAV_SERVER_PASSWORD);
        http.getState().setCredentials(null, null, credentials);
        http.getState().setAuthenticationPreemptive(true);
        return http;
    }
    
    public HostConfiguration createHostConfiguration(){
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(getCalDAVServerHost(), getCalDAVServerPort());
        return hostConfig;
    }
    
    protected Calendar getCalendarResource(String resourceName) {
        Calendar cal;

        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream(resourceName);
        CalendarBuilder cb = new CalendarBuilder();
        
        try {
            cal = cb.build(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return cal;
    }    
    
    protected void put(String resourceFileName, String path){
        PutMethod put = methodFactory.createPutMethod();
        InputStream stream = this.getClass().getClassLoader()
        .getResourceAsStream(resourceFileName);
        put.setRequestBody(stream);
        put.setPath(path);
        try {
            http.executeMethod(hostConfig, put);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    protected void del(String path){
        DeleteMethod delete = new DeleteMethod();
        delete.setPath(path);
        try {
        http.executeMethod(hostConfig, delete);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    protected void mkdir(String path){
        MkCalendarMethod mk = new MkCalendarMethod();
        mk.setPath(path);
        try {
        http.executeMethod(hostConfig, mk);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
