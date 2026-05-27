package com.hongbang.pojo;

import java.util.List;

// 公告实体类
public class Announcement {
    private PublicNotice publicNotice;
    private List<PublicFile> files;

    public Announcement(PublicNotice publicNotice, List<PublicFile> files) {
        this.publicNotice = publicNotice;
        this.files = files;
    }

    public PublicNotice getPublicNotice() {
        return publicNotice;
    }

    public void setPublicNotice(PublicNotice publicNotice) {
        this.publicNotice = publicNotice;
    }

    public List<PublicFile> getFiles() {
        return files;
    }

    public void setFiles(List<PublicFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "publicNotice=" + publicNotice +
                ", files=" + files +
                '}';
    }


}


//// 数据处理工具类
//public class AnnouncementProcessor {
//    public static List<Map<String, Object>> processAnnouncements(List<Announcement> announcements) {
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        for (Announcement ann : announcements) {
//            Map<String, Object> annMap = new LinkedHashMap<>();
//            PublicNotice publicNotice = ann.getPublicNotice();
//            annMap.put("PublicNotice",publicNotice);
//
//            // 处理文件列表
//            List<Map<String, Object>> filesList = new ArrayList<>();
//            for (PublicFile file : ann.getFiles()) {
//                Map<String, Object> fileMap = new LinkedHashMap<>();
//                fileMap.put("id", file.getId());
//                fileMap.put("name", file.getName());
//                fileMap.put("size", file.getSize());
//                fileMap.put("type", file.getType());
//                filesList.add(fileMap);
//            }
//            annMap.put("files", filesList);
//
//            result.add(annMap);
//        }
//
//        return result;
//    }
//}