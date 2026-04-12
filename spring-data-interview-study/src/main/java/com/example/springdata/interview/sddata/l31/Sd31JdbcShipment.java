package com.example.springdata.interview.sddata.l31;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** Uppercase name matches H2 default unquoted DDL (avoids quoted "sd31_shipments" vs SD31_SHIPMENTS mismatch). */
@Table("SD31_SHIPMENTS")
public class Sd31JdbcShipment {

    @Id
    private Long id;

    private String trackingCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }
}
