package com.puxiang.mall.model.data;

public class RxTasks {
    private int id;
    private int sort;
    private String taskIntroduce;
    private int taskQty;
    private int taskType;
    private int taskBusinessId;
    private int taskState;
    private double taskUnitScore;
    private String taskCode;
    private String taskName;
    private String url;
    private boolean completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTaskIntroduce() {
        return taskIntroduce;
    }

    public void setTaskIntroduce(String taskIntroduce) {
        this.taskIntroduce = taskIntroduce;
    }

    public int getTaskQty() {
        return taskQty;
    }

    public void setTaskQty(int taskQty) {
        this.taskQty = taskQty;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskBusinessId() {
        return taskBusinessId;
    }

    public void setTaskBusinessId(int taskBusinessId) {
        this.taskBusinessId = taskBusinessId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public double getTaskUnitScore() {
        return taskUnitScore;
    }

    public void setTaskUnitScore(double taskUnitScore) {
        this.taskUnitScore = taskUnitScore;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
