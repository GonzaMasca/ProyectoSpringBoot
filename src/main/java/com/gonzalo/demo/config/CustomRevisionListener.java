package com.gonzalo.demo.config;

import org.hibernate.envers.RevisionListener;

import com.gonzalo.demo.entities.audit.Revision;

public class CustomRevisionListener implements RevisionListener {
    public void newRevision(Object revisionEntity) {
        final Revision revision = (Revision) revisionEntity;
    }
}