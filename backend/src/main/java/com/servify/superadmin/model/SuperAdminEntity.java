package com.servify.superadmin.model;

import com.servify.admin.model.AdminEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "superadmin")
public class SuperAdminEntity extends AdminEntity {

}
