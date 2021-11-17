<template>
  <div class="chatTitle">
    <span class="titleName">{{this.$store.state.currentSession.nickname?this.$store.state.currentSession.nickname:""}}</span>
    <el-button class="moreBtn" @click="exitSystem" size="small" icon="el-icon-close"></el-button>
  </div>
</template>

<script>
  export default {
    name: "chattitle",

    methods:{
      //退出系统
      exitSystem(){
        this.$confirm('你是否要退出系统吗?', '系统提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getRequest("/logout");
          sessionStorage.removeItem("user");
          //清除SessionStorage中保存的state
          if (sessionStorage.getItem("state")) {
            sessionStorage.removeItem("state");
          }
          //关闭连接
          this.$store.dispatch("disconnect");
          this.$router.replace("/");
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      }
    }
  }
</script>

<style scoped>
  .chatTitle{
    height: 50px;
    width: 100%;
    font-size: 20px;
    display: flex;
    justify-content: space-between;
    border-bottom: 0.5px solid #c7d2db;
  }
  .moreBtn{
    background-color: #eee;
    border: 0;
    height: 40px;
  }
  .titleName{
    margin: 10px 20px;
  }

</style>
