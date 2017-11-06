package net.qiujuer.italker.factory.model.card;

import java.util.Date;

/**
 * 申请请求的Card, 用于推送一个申请请求
 *
 * @author qiujuer Email:qiujuer.live.cn
 */
public class ApplyCard {
    // 申请Id
    private String id;
    // 附件
    private String attach;
    // 描述
    private String desc;
    // 目标的类型
    private int type;
    // 目标（群／人...的ID）
    private String targetId;
    // 申请人的Id
    private String applicantId;
    // 创建时间
    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
