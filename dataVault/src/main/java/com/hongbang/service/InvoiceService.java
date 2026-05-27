package com.hongbang.service;

import com.hongbang.pojo.FormRk;
import com.hongbang.pojo.Invoice;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
    //根据appId查询发票信息
    List<Invoice> selectFp( int appId);

    //插入多张发票信息
    void insertAllInvoice(List<FormRk> formRks, List<Invoice> invoices);

    //查询未审核发票的申请单信息
    List<Map<String,Object>> selectWsFp(Map<String,Object> maps);

    //通过审核发票信息
    void updatePass(int appId,String time);

    //不通过发票信息
    void updateNoPass( int appId,String time);
}
