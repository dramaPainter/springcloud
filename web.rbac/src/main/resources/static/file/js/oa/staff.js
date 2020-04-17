appLoad(() => {
    var vue = new Vue({
        el: '#app',
        data: {
            tableData: [],
            rowCount: 0,
            page: 1,
            status: null,
            searchType: null,
            searchText: "",
            staff: { userid: 0, username: '', loginpass: '', nickname: '', headimage: '', type: null, status: null },
            loading: true,
            dialogTitle: "",
            dialogEnabled: false,
            username_disabled: false,
            enums: enums
        },
        methods: {
            toEdit(row) {
                this.dialogTitle = "修改帐号";
                this.username_disabled = true;
                this.staff = row;
                this.dialogEnabled = true;
            },
            toDelete(row) {
                let status = row.status === enums.status.ENABLE.value ? enums.status.DISABLE : enums.status.ENABLE;
                this.$confirm("确定要" + status.name + "账户" + row.username + "?", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'danger'
                }).then(() => {
                    loadData("post", "/oa/changeStaffStatus", {userid: row.userid, status: status.value}, r => {
                        this.$alert(r.message, '温馨提示');
                        this.search();
                    }, e => {
                        this.$alert(e.message, '温馨提示');
                    });
                });
            },
            pageChanged(pageid) {
                this.page = pageid;
                this.search();
            },
            btnSearchClick() {
                this.pageChanged(1);
            },
            search() {
                this.loading = true;
                var url = "/oa/staff";
                url += "?page=" + this.page + "&text=" + escape(this.searchText)
                    + "&status=" + (this.status == null ? 0 : this.status)
                    + "&type=" + (this.searchType == null ? 0 : this.searchType);
                loadData("get", url, {page: this.page}, r => {
                    this.tableData = r.data;
                    this.rowCount = r.code;
                    this.loading = false;
                }, e => {
                    this.loading = false;
                    this.$alert(e.message, '温馨提示');
                });
            },
            showInsertDialog() {
                this.dialogTitle = "添加帐号";
                this.username_disabled = false;
                this.staff = { userid: 0, username: '', loginpass: '', nickname: '', headimage: '', type: null, status: null };
                this.dialogEnabled = true;
            },
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    });

    vue.search();
});