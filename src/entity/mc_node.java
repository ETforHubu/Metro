package entity;

public class mc_node {
    String sNumber;
    String id;
    String parentId;
    String title;
    String note;
    double quantity;
    String unit;
    double constructionCost;
    double installCost;
    double deviceCost;
    double otherCost;
    double quota;
    String Code;
    String other;
    String sort;
    String value;

    public mc_node() {
    }

//    @Override
//    public String toString() {
//        return "Book{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", author='" + author + '\'' +
//                ", price=" + price +
//                '}';
//    }

    public mc_node(String sNumber,String id, String parentId,String title,String note,double quantity,String unit,double constructionCost,double installCost,double deviceCost,double otherCost,double quota,String Code,String other,String sort,String value) {
        this.sNumber = sNumber;
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.unit = unit;
        this.quantity = quantity;
        this.constructionCost = constructionCost;
        this.installCost = installCost;
        this.deviceCost = deviceCost;
        this.otherCost = otherCost;
        this.quota = quota;
        this.Code = Code;
        this.other = other;
        this.sort = sort;
        this.value = value;
        this.note = note;
    }
    //id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    //parentId
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    //title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    //sNumber
    public String getsNumber() {
        return sNumber;
    }
    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }
    //unit
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    //quantity
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    //constructionCost
    public double getConstructionCost() {
        return constructionCost;
    }
    public void setConstructionCost(double constructionCost) {
        this.constructionCost = constructionCost;
    }
    //installCost
    public double getInstallCost() {
        return installCost;
    }
    public void setInstallCost(double installCost) {
        this.installCost = installCost;
    }
    //deviceCost
    public double getDeviceCost() {
        return deviceCost;
    }
    public void setDeviceCost(double deviceCost) {
        this.deviceCost = deviceCost;
    }
    //otherCost
    public double getOtherCost() {
        return otherCost;
    }
    public void setOtherCost(double otherCost) {
        this.otherCost = otherCost;
    }
    //quota
    public double getQuota() {
        return quota;
    }
    public void setQuota(double quota) {
        this.quota = quota;
    }
    //Code
    public String getCode() {
        return Code;
    }
    public void setCode(String Code) {
        this.Code = Code;
    }
    //other
    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    //sort
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    //sort
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    //note
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
