package com.msi.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MSI on 18/12/21.
 */

public class Province extends DataSupport {
    private int id;

    private String provinceName;

    private int provinceCode;

    /**
     * Get方法
     * @return
     */
    public int getId() {
        return id;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public int getProvinceCode() {
        return provinceCode;
    }

    /**
     * Set方法
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
