package cn.addenda.footprints.core.interceptor.baseentity;

import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import lombok.*;

import java.util.Stack;

/**
 * @author addenda
 * @since 2023/5/3 18:13
 */
public class BaseEntityContext {

    private BaseEntityContext() {
    }

    private static final ThreadLocal<Stack<BaseEntityConfig>> BASE_ENTITY_CONFIG_TL = ThreadLocal.withInitial(() -> null);

    public static void setDisable(boolean disable) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setDisable(disable);
    }

    public static Boolean getDisable() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getDisable();
    }

    public static void setMasterView(String masterView) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setMasterView(masterView);
    }

    public static String getMasterView() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getMasterView();
    }

    public static void setReportItemNameExists(boolean reportItemNameExists) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setReportItemNameExists(reportItemNameExists);
    }

    public static Boolean getReportItemNameExists() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getReportItemNameExists();
    }

    public static void setDuplicateKeyUpdate(boolean duplicateKeyUpdate) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setDuplicateKeyUpdate(duplicateKeyUpdate);
    }

    public static Boolean getDuplicateKeyUpdate() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getDuplicateKeyUpdate();
    }

    public static void setInsertSelectAddItemMode(InsertSelectAddItemMode insertSelectAddItemMode) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setInsertSelectAddItemMode(insertSelectAddItemMode);
    }

    public static InsertSelectAddItemMode getInsertSelectAddItemMode() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getInsertSelectAddItemMode();
    }

    public static void setUpdateItemMode(UpdateItemMode updateItemMode) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        baseEntityConfig.setUpdateItemMode(updateItemMode);
    }

    public static UpdateItemMode getUpdateItemMode() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        BaseEntityConfig baseEntityConfig = baseEntityConfigs.peek();
        return baseEntityConfig.getUpdateItemMode();
    }

    public static void push() {
        push(new BaseEntityConfig());
    }

    public static void pop() {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        baseEntityConfigs.pop();
        if (baseEntityConfigs.isEmpty()) {
            BASE_ENTITY_CONFIG_TL.remove();
        }
    }

    public static BaseEntityConfig peek() {
        return BASE_ENTITY_CONFIG_TL.get().peek();
    }

    public static boolean contextActive() {
        return BASE_ENTITY_CONFIG_TL.get() != null;
    }

    public static void push(BaseEntityConfig baseEntityConfig) {
        Stack<BaseEntityConfig> baseEntityConfigs = BASE_ENTITY_CONFIG_TL.get();
        if (baseEntityConfigs == null) {
            baseEntityConfigs = new Stack<>();
            BASE_ENTITY_CONFIG_TL.set(baseEntityConfigs);
        }
        baseEntityConfigs.push(baseEntityConfig);
    }

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaseEntityConfig {
        private Boolean disable;
        private String masterView;
        private Boolean reportItemNameExists;
        private Boolean duplicateKeyUpdate;
        private InsertSelectAddItemMode insertSelectAddItemMode;
        private UpdateItemMode updateItemMode;

        public BaseEntityConfig(BaseEntityConfig baseEntityConfig) {
            this.disable = baseEntityConfig.disable;
            this.masterView = baseEntityConfig.masterView;
            this.reportItemNameExists = baseEntityConfig.reportItemNameExists;
            this.duplicateKeyUpdate = baseEntityConfig.duplicateKeyUpdate;
            this.insertSelectAddItemMode = baseEntityConfig.insertSelectAddItemMode;
            this.updateItemMode = baseEntityConfig.updateItemMode;
        }
    }

}
