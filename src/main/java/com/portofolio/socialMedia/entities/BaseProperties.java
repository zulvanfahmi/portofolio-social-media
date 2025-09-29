package com.portofolio.socialMedia.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseProperties {

    @Column(nullable = false, updatable = false)
    private Long created_by;

    @Column(
        nullable = false, 
        updatable = false, 
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date created_on = new Date();

    @Column
    private Long modified_by;

    @Column
    private Date modified_on;

    @Column
    private Long deleted_by;

    @Column
    private Date deleted_on;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean is_delete = false;

    @PreUpdate
    protected void onUpdate() {
        modified_on = new Date();
    }

}
