package org.gitee.ztkyn.easyexcel.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-30 15:12
 * @description DataDefinition
 */
@Setter
@Getter
@Accessors(chain = true)
public class DataDefinition {

    private static final Logger logger = LoggerFactory.getLogger(DataDefinition.class);

    private String packageName;

    private String className;

    private String description;

    private List<DataConfiguration> configurations;

    public String getFullName() {
        return packageName + "." + className;
    }

}
