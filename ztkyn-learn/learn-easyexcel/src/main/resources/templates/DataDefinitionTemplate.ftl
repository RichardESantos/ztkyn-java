package ${packageName};


<#--import lombok.Getter;-->
<#--import lombok.Setter;-->
<#--import lombok.experimental.Accessors;-->
import com.alibaba.excel.annotation.ExcelProperty;

<#--@Setter-->
<#--@Getter-->
<#--@Accessors(chain = true)-->
public class ${className}{

    <#list configurations as config>
        @ExcelProperty("${config.headerName}")
        private ${config.valueType} ${config.valueName};

        public void set${config.valueName?cap_first}(${config.valueType} ${config.valueName}){
            this.${config.valueName} = ${config.valueName};
        }

        public ${config.valueType} get${config.valueName?cap_first}(){
            return this.${config.valueName};
        }

    </#list>
}