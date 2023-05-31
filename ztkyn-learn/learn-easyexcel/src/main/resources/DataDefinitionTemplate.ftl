package org.gitee.ztkyn.gen;

public class ${dataDefinition.className}{

    <#list dataDefinition.configurations as config>
        @ExcelProperty("${config.headerName}")
        private   ${config.valueType}      ${config.valueName};

        public void set${config.valueName}(${config.valueType}      ${config.valueName}){
            this.${config.valueName} = ${config.valueName};
        }

        public ${config.valueType} get${config.valueName}(){
            return this.${config.valueName};
        }
    </#list>
}