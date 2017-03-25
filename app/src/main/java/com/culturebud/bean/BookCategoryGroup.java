package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XieWei on 2017/3/25.
 */

public class BookCategoryGroup {

    @SerializedName("relationType")
    private int relationType;
    @SerializedName("total")
    private int total;
    @SerializedName("categoryGroupList")
    private List<CategoryGroup> categoryGroups;

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CategoryGroup> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(List<CategoryGroup> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    @Override
    public String toString() {
        return "BookCategoryGroup{" +
                "relationType=" + relationType +
                ", total=" + total +
                ", categoryGroups=" + categoryGroups +
                '}';
    }

    public class CategoryGroup {
        @SerializedName("categoryType")
        private int categoryType;
        @SerializedName("categoryTypeName")
        private String categoryTypeName;
        @SerializedName("categoryStatisList")
        private List<Category> categoryStatistics;

        public int getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(int categoryType) {
            this.categoryType = categoryType;
        }

        public String getCategoryTypeName() {
            return categoryTypeName;
        }

        public void setCategoryTypeName(String categoryTypeName) {
            this.categoryTypeName = categoryTypeName;
        }

        public List<Category> getCategoryStatistics() {
            return categoryStatistics;
        }

        public void setCategoryStatistics(List<Category> categoryStatistics) {
            this.categoryStatistics = categoryStatistics;
        }

        @Override
        public String toString() {
            return "CategoryGroup{" +
                    "categoryType=" + categoryType +
                    ", categoryTypeName='" + categoryTypeName + '\'' +
                    ", categoryStatistics=" + categoryStatistics +
                    '}';
        }
    }

    public class Category {
        @SerializedName("category")
        private String category;
        @SerializedName("statis")
        private int statistics;
        private CategoryGroup group;

        public CategoryGroup getGroup() {
            return group;
        }

        public void setGroup(CategoryGroup group) {
            this.group = group;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getStatistics() {
            return statistics;
        }

        public void setStatistics(int statistics) {
            this.statistics = statistics;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "category='" + category + '\'' +
                    ", statistics=" + statistics +
                    '}';
        }
    }
}
