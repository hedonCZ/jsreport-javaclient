package net.jsreport.java.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Handles all values about service call timeout.
 * <br/>
 *
 * For each value <tt>0</tt> means timeout is <tt>disabled</tt>.
 * <br/>
 *
 * @see <a href="https://howtodoinjava.com/retrofit2/connection-timeout-exception">Retrofit 2 – Handle Connection Timeout Exception</a>
 * */
public class ServiceTimeout {

    /**
     * Whole call (start connection + send data + receiving response) timeout. Default value is <b><tt>0</tt></b> (disabled).
     * */
    private Duration callTimeout;

    /** Timeout for start connection to server. Default value is <b><tt>10 seconds</tt></b>. */
    private Duration connectTimeout;

    /** Timeout for sending data to server. Default value is <b><tt>10 seconds</tt></b>.*/
    private Duration writeTimeout;

    /** Timeout for receving data from server. Default value is <b><tt>10 seconds</tt></b>.*/
    private Duration readTimeout;

    /** Set default timeouts as are set in Retrofit2 library. */
    public ServiceTimeout() {
        callTimeout = Duration.of(0, ChronoUnit.MILLIS);
        connectTimeout = Duration.of(10, ChronoUnit.SECONDS);
        writeTimeout = Duration.of(10, ChronoUnit.SECONDS);
        readTimeout = Duration.of(10, ChronoUnit.SECONDS);
    }

    public Duration getCallTimeout() {
        return callTimeout;
    }

    public ServiceTimeout setCallTimeout(Duration callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public ServiceTimeout setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public ServiceTimeout setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public ServiceTimeout setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
}
