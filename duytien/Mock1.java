package r2hello.tutorial.action;

import src.main.java.r2hello.tutorial.dto.UserDto;
import src.main.java.r2hello.tutorial.dto.resultset.DeptDto;
import src.main.java.r2hello.tutorial.dto.resultset.ProjectDto;
import src.main.java.r2hello.tutorial.dto.sqlParam.ProjectParamDto;
import src.main.java.r2hello.tutorial.enums.EditMode;
import src.main.java.r2hello.tutorial.form.ProjectForm;

/**
 * 繝励Ο繧ｸ繧ｧ繧ｯ繝医い繧ｯ繧ｷ繝ｧ繝ｳ縺ｧ縺吶�繝励Ο繧ｸ繧ｧ繧ｯ繝医�讀懃ｴ｢縲∫匳骭ｲ縲∵峩譁ｰ縲∝炎髯､繧貞�逅�＠縺ｾ縺吶�
 * 
 * @author nishinaka
 */
public class ProjectAction {

    // =========================================================================
    //                                                                DI
    //                                                                ==========
    @ActionForm
    @Resource
    protected ProjectForm projectForm;

    @Resource
    protected JdbcManager jdbcManager;

    @Resource
    protected Paginator paginator;

    @Resource
    protected SequenceService sequenceService;

    @Session(store="sessiondb")
    protected UserDto userDto;
    
    // =========================================================================
    //                                                                繝励Ο繝代ユ繧｣
    //                                                                ==========
    /** 繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ陦ｨ遉ｺ逕ｨ繝ｪ繧ｹ繝�*/
    public List<ProjectDto> projectList;    

    /** 驛ｨ髢�そ繝ｬ繧ｯ繝医�繝�け繧ｹ陦ｨ遉ｺ逕ｨ繝ｪ繧ｹ繝�*/
    public List<DeptDto> deptList;
    
    /** 逋ｻ骭ｲ/譖ｴ譁ｰ/蜑企勁  陦ｨ遉ｺ蛻�ｊ譖ｿ縺育畑繝励Ο繝代ユ繧｣ */
    public EditMode editMode;
    
    // =========================================================================
    //                                                              螳溯｡後Γ繧ｽ繝�ラ
    //                                                                ==========

    /**
     * 繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ繧定｡ｨ遉ｺ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    public String index() {

        // 蛻晄悄陦ｨ遉ｺ繝｡繝�そ繝ｼ繧ｸ(<r2:messages/>縺ｧ蜃ｺ蜉帙＆繧後ｋ)
        R2ActionMessagesUtil.addMessageToRequest(ActionMessages.GLOBAL_MESSAGE,
                "messages.project.list.init");

        // 繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ讀懃ｴ｢(讀懃ｴ｢譚｡莉ｶ縺ｪ縺励〒讀懃ｴ｢)
        setProjectList();

        return showListInit();
    }

    /**
     * 繝励Ο繧ｸ繧ｧ繧ｯ繝医�讀懃ｴ｢蜃ｦ逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(input = "backToList", validator = false)
    public String doSearch() {
        // 蜈･蜉帙ヵ繧ｩ繝ｼ繝�メ繧ｧ繝�け
        checkSearchCondition();
        // 繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ讀懃ｴ｢
        setProjectList();

        return showListInit();
    }

    /**
     * 荳�ｦｧ逕ｻ髱｢縺ｫ謌ｻ繧句�逅�ｒ陦後＞縺ｾ縺吶�
     * @return
     */
    @Execute(validator = false)
    public String backToList() {
        return showListInit();
    }
    
    /**
     * 譁ｰ隕冗匳骭ｲ縺ｮ蛻晄悄陦ｨ遉ｺ繧偵＠縺ｾ縺吶�
     * @return
     */
    @Execute(validator = false)
    public String showRegister() {
        setDeptList();
        return "register.jsp";
    }

    /**
     * 譁ｰ隕冗匳骭ｲ縺ｮ遒ｺ隱榊�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false, input = "backToRegister")
    @Token
    public String doRegisterConfirm() {
        checkEditCondition();
        setDeptList();
        editMode = EditMode.REGISTER;
        return "confirm.jsp";
    }

    /**
     * 譁ｰ隕冗匳骭ｲ縺ｮ螳御ｺ��逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false, input = "backToRegister")
    @TokenCheck
    public String doRegisterComplete() {
        // hidden縺ｧ貂｡縺｣縺ｦ縺上ｋ繝輔か繝ｼ繝��縺ｮ繝√ぉ繝�け
        checkEditCondition();
        ProjectDto project = R2Beans.createAndCopy(ProjectDto.class, projectForm)
                .execute();

        // SequenceService繧貞茜逕ｨ縺励※繧ｷ繝ｼ繧ｱ繝ｳ繧ｹ蛟､繧貞ｾ励∪縺�
        project.projectId = sequenceService.getNext("PROJECT_ID");
        // 譁ｰ隕冗匳骭ｲ縺ｮ縺溘ａ繝舌�繧ｸ繝ｧ繝ｳ蛟､縺ｯ'1'
        project.version = 1L;
        // 2WaySQL縺ｫ縺翫＞縺ｦ縲∫匳骭ｲ縲∵峩譁ｰ縲∝炎髯､縺ｯ縺吶∋縺ｦ縲蛍pdate縲阪Γ繧ｽ繝�ラ縺ｫ縺ｪ繧翫∪縺�
        jdbcManager.updateBySqlFile("r2hello/tutorial/action/ProjectAction_doRegisterComplete_Ins.sql",
                project).execute();
        return "showRegisterComplete/?redirect=true";
    }

    /**
     * 譁ｰ隕冗匳骭ｲ縺ｮ螳御ｺ�判髱｢繧定｡ｨ遉ｺ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    public String showRegisterComplete() {
        editMode = EditMode.REGISTER;
        return "complete.jsp";
    }

    /**
     * 譁ｰ隕冗匳骭ｲ縺ｮ蛻晄悄陦ｨ遉ｺ逕ｻ髱｢縺ｫ謌ｻ繧句�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    @Token
    public String backToRegister() {
        return showRegister();
    }

    /**
     * 譖ｴ譁ｰ縺ｮ蛻晄悄陦ｨ遉ｺ繧偵＠縺ｾ縺吶�
     * @return
     */
    @Execute(validator = false, urlPattern = "showUpdate/{projectId}")
    public String showUpdate() {

        // 讀懃ｴ｢縲ＥisallowNoResult繧定ｨｭ螳壹＠縺ｦ縺�ｋ縺溘ａ縲√Ξ繧ｳ繝ｼ繝峨′隕九▽縺九ｉ縺ｪ縺九▲縺溘ｉ萓句､悶ｒ謚輔￡縺ｾ縺�
        ProjectDto project = jdbcManager
                .selectBySqlFile(ProjectDto.class,
                        "r2hello/tutorial/action/ProjectAction_showUpdate_Sel.sql", projectForm.projectId)
                .disallowNoResult().getSingleResult();

        R2Beans.copy(project, projectForm).execute();
        setDeptList();
        return "update.jsp";
    }

    /**
     * 譖ｴ譁ｰ縺ｮ遒ｺ隱榊�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false, input = "backToUpdate")
    @Token
    public String doUpdateConfirm() {
        checkEditCondition();
        setDeptList();6789--------------
    public String showUpdateComplete() {
        editMode = EditMode.UPDATE;
        return "complete.jsp";
    }

    /**
     * 譖ｴ譁ｰ縺ｮ蛻晄悄陦ｨ遉ｺ逕ｻ髱｢縺ｫ謌ｻ繧句�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    @Token
    public String backToUpdate() {
        return showUpdate();
    }

    /**
     * 蜑企勁縺ｮ遒ｺ隱榊�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false, input = "backToDelete", urlPattern = "doDeleteConfirm/{projectId}")
    @Token
    public String doDeleteConfirm() {
        checkDeleteCondition();
        // 讀懃ｴ｢縲ＥisallowNoResult繧定ｨｭ螳壹＠縺ｦ縺�ｋ縺溘ａ縲√Ξ繧ｳ繝ｼ繝峨′隕九▽縺九ｉ縺ｪ縺九▲縺溘ｉ萓句､悶ｒ謚輔￡縺ｾ縺�
        ProjectDto project = jdbcManager
                .selectBySqlFile(ProjectDto.class,
                        "r2hello/tutorial/action/ProjectAction_doDeleteConfirm_Sel.sql", projectForm.projectId)
                .disallowNoResult().getSingleResult();

        R2Beans.copy(project, projectForm).execute();
        setDeptList();
        editMode = EditMode.DELETE;
        return "confirm.jsp";
    }

    /**
     * 蜑企勁縺ｮ螳御ｺ��逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false, input = "backToDelete")
    @TokenCheck
    public String doDeleteComplete() {
        // hidden縺ｧ貂｡縺｣縺ｦ縺上ｋ繝輔か繝ｼ繝��繧偵メ繧ｧ繝�け
        checkDeleteCondition();
        ProjectDto project = R2Beans.createAndCopy(ProjectDto.class, projectForm)
                .execute();
        jdbcManager.updateBatchBySqlFile(
                "r2hello/tutorial/action/ProjectAction_doDeleteComplete_Del.sql", project).execute();
        return "showDeleteComplete/?redirect=true";
    }

    /**
     * 蜑企勁縺ｮ螳御ｺ�判髱｢繧定｡ｨ遉ｺ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    public String showDeleteComplete() {
        editMode = EditMode.DELETE;
        return "complete.jsp";
    }

    /**
     * 蜑企勁縺ｮ蛻晄悄陦ｨ遉ｺ逕ｻ髱｢縺ｫ謌ｻ繧句�逅�ｒ縺励∪縺吶�
     * @return
     */
    @Execute(validator = false)
    @Token
    public String backToDelete() {
        // 蜑企勁逕ｨ縺ｮ蛻晄悄陦ｨ遉ｺ逕ｻ髱｢縺ｯ迚ｹ縺ｫ縺ｪ縺��縺ｧ繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ逕ｻ髱｢縺ｫ謌ｻ縺励∪縺�
        return "/project/?redirect=true";
    }

    // =========================================================================
    //                                                            荳玖ｫ九￠繝｡繧ｽ繝�ラ
    //                                                                  ========    

    /**
     * 繝励Ο繧ｸ繧ｧ繧ｯ繝井ｸ�ｦｧ陦ｨ遉ｺ縺ｮ蛻晄悄蜃ｦ逅�ｒ縺励∪縺吶�
     * @return
     */
    private String showListInit() {
        setDeptList();
        return "list.jsp";
    }

    /**
     * 繝励Ο繧ｸ繧ｧ繧ｯ繝医ｒ讀懃ｴ｢縺励※繝ｪ繧ｹ繝医↓繧ｻ繝�ヨ縺励∪縺吶�
     */
    private void setProjectList() {

        //SQL繝代Λ繝｡繝ｼ繧ｿ繧定ｨｭ螳�
        ProjectParamDto param = R2Beans.createAndCopy(ProjectParamDto.class, projectForm).execute();
        
        // 蜷郁ｨ井ｻｶ謨ｰ繧貞叙蠕�
        long total = jdbcManager.getCountBySqlFile("r2hello/tutorial/action/ProjectAction_setProjectList_Sel.sql", param);

        // 繝壹�繧ｸ繝ｳ繧ｰ逕ｨ險ｭ螳�
        paginator.setItemCount(total);
        paginator.setParams(projectForm.pn);
        paginator.setRequestUri("doSearch");
          
        // 讀懃ｴ｢
        projectList = jdbcManager
                .selectBySqlFile(ProjectDto.class,
                        "r2hello/tutorial/action/ProjectAction_setProjectList_Sel.sql", param)
                .offset(paginator.getOffset())
                .limit(paginator.getLimit())
                .getResultList();

        // 讀懃ｴ｢邨先棡縺檎ｩｺ縺ｮ蝣ｴ蜷医�繝｡繝�そ繝ｼ繧ｸ(<r2:messages/>縺ｧ蜃ｺ蜉帙＆繧後ｋ)
        if (projectList.isEmpty()) {
            R2ActionMessagesUtil.addMessageToRequest(ActionMessages.GLOBAL_MESSAGE,
                    "messages.project.list.empty");
        }
    }

    /**
     * 驛ｨ髢�ｒ讀懃ｴ｢縺励※繝ｪ繧ｹ繝医↓繧ｻ繝�ヨ縺励∪縺吶�
     */
    private void setDeptList() {
        deptList = jdbcManager.selectBySqlFile(DeptDto.class,
                "r2hello/tutorial/action/ProjectAction_setDeptList_Sel.sql").getResultList();
    }

    // =========================================================================
    //                                                          繝√ぉ繝�け繝｡繧ｽ繝�ラ
    //                                                                  ========

    /**
     * 讀懃ｴ｢縺ｮ繝輔か繝ｼ繝��縺ｮ讀懆ｨｼ繧偵＠縺ｾ縺吶�
     */
    private void checkSearchCondition() {
        ProjectForm form = ActionFormManager.validateFor(ProjectForm.class);
        ActionFormManager
            .validation(form.projectId, numericCheck())
            .validation(form.projectNm, maxLengthCheck(30))
            .validation(form.deptId, numericCheck())
            .validation(form.difficulty, numericCheck())
            .execute();
        if(ActionMessagesUtil.hasErrors()) {
            throw new R2ActionMessagesException(R2ActionMessagesUtil.getErrors());
        }
    }

    /**
     * 蜑企勁縺ｮ繝輔か繝ｼ繝��縺ｮ讀懆ｨｼ繧偵＠縺ｾ縺吶�
     */
    private void checkDeleteCondition() {
        ProjectForm form = ActionFormManager.validateFor(ProjectForm.class);
        ActionFormManager
            .validation(form.projectId, emptyCheck(), numericCheck())
            .execute();
        if(R2ActionMessagesUtil.hasErrors()) {
            throw new R2ActionMessagesException(R2ActionMessagesUtil.getErrors());
        }        
    }
    
    /**
     * 逋ｻ骭ｲ縲∵峩譁ｰ縺ｮ繝輔か繝ｼ繝��縺ｮ讀懆ｨｼ繧偵＠縺ｾ縺吶�
     */
    private void checkEditCondition() {
        ProjectForm form = ActionFormManager.validateFor(ProjectForm.class);
        ActionFormManager
            .validation(form.projectNm, emptyCheck(), maxLengthCheck(30))
            .validation(form.deptId, emptyCheck(), numericCheck())
            .validation(form.difficulty, emptyCheck(), numericCheck())
            .execute();
        if(R2ActionMessagesUtil.hasErrors()) {
            throw new R2ActionMessagesException(R2ActionMessagesUtil.getErrors());
        }
    }
}
