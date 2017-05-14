package shared;

import shared.business_entities.BusinessEntity;
import shared.business_entities.Project;

import java.io.Serializable;

/**
 * Created by Nicolai Onov on 5/10/2017.
 */
public class UpdateMessage implements Serializable {
    private String header;
    private BusinessEntity entity;

    public UpdateMessage(String header, BusinessEntity entity) {
        this.header = header;
        this.entity = entity;
    }

    public String getHeader() {
        return header;
    }

    public BusinessEntity getBusinessEntity() {
        return this.entity;
    }
}
