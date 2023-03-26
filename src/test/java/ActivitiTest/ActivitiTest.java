package ActivitiTest;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;


public class ActivitiTest {

    @Test
    public void testCreateDbTable() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/demoMyLeave.bpmn20.xml").addClasspathResource("bpmn/demoMyLeave.png").name("请假申请流程").deploy();
        System.out.println("流程部署id"+deployment.getId());
        System.out.println("流程部署名称："+deployment.getName());
    }
    @Test
    public void testRunActiviti() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance demoMyLeave = runtimeService.startProcessInstanceByKey("demoMyLeave");
        System.out.println("流程定义ID"+demoMyLeave.getProcessDefinitionId());
        System.out.println("流程实例ID："+demoMyLeave.getId());
        System.out.println("当前活动ID："+demoMyLeave.getActivityId());
    }
    @Test
    public void testFindPersonalTask(){
        String assignee="worker";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> demoLeave = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee(assignee).list();
        for (Task task : demoLeave) {
            System.out.println("流程实例ID:"+task.getProcessDefinitionId());
            System.out.println("任务ID:"+task.getId());
            System.out.println("任务负责人:"+task.getAssignee());
            System.out.println("任务名称:"+task.getName());
        }
    }
    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
//        Task task = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("worker").singleResult();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("worker").list();
        for (Task task : list) {
            taskService.complete(task.getId());
            System.out.println("worker完成请假申请"+task.getId());
        }
    }

    @Test
    public void managerTestFindPersonalTask(){
        String assignee="manager";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> demoLeave = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee(assignee).list();
        for (Task task : demoLeave) {
            System.out.println("流程实例ID:"+task.getProcessDefinitionId());
            System.out.println("任务ID:"+task.getId());
            System.out.println("任务负责人:"+task.getAssignee());
            System.out.println("任务名称:"+task.getName());
        }
    }
    @Test
    public void managerCompleteTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
//        Task task = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("manager").singleResult();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("manager").list();
        for (Task task : list) {
            taskService.complete(task.getId());
            System.out.println("部门经理完成请假审批");
        }
    }

    @Test
    public void financerTestFindPersonalTask(){
        String assignee="financer";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> demoLeave = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee(assignee).list();
        for (Task task : demoLeave) {
            System.out.println("流程实例ID:"+task.getProcessDefinitionId());
            System.out.println("任务ID:"+task.getId());
            System.out.println("任务负责人:"+task.getAssignee());
            System.out.println("任务名称:"+task.getName());
        }
    }
    @Test
    public void financeCompleteTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
//        Task task = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("financer").singleResult();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("demoMyLeave").taskAssignee("financer").list();
        for (Task task : list) {
            taskService.complete(task.getId());
            System.out.println("财务人员完成财务审批");
        }
    }
    @Test
    public void queryProcessInstance(){
     String processDefinitionKey = "demoMyLeave";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).list();
        System.out.println("当前的业务流程为："+list.toString());
        for (ProcessInstance processInstance : list) {
            System.out.println("----------------------");
            System.out.println("流程实例ID:"+processInstance.getProcessInstanceId());
            System.out.println("所属流程定义ID:"+processInstance.getProcessDefinitionId());
            System.out.println("当前是否完成:"+processInstance.isEnded());
            System.out.println("是否暂停:"+processInstance.isSuspended());
            System.out.println("当前活动标识:"+processInstance.getActivityId());
            System.out.println("业务关键字:"+processInstance.getBusinessKey());
            System.out.println("----------------------");
        }
    }

    @Test
    public void queryHis(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        historicActivityInstanceQuery.processInstanceId("27501");
//        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println("历史活动ID："+historicActivityInstance.getActivityId());
            System.out.println("历史活动名称："+historicActivityInstance.getActivityName());
            System.out.println("历史流程定义ID："+historicActivityInstance.getProcessDefinitionId());
            System.out.println("历史流程实例ID："+historicActivityInstance.getProcessInstanceId());
            System.out.println("===============分隔符===============");
        }
    }
}
