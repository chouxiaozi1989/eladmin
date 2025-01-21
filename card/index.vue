<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">cardId</label>
        <el-input v-model="query.cardId" clearable placeholder="cardId" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">userId</label>
        <el-input v-model="query.userId" clearable placeholder="userId" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">realName</label>
        <el-input v-model="query.realName" clearable placeholder="realName" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">mobile</label>
        <el-input v-model="query.mobile" clearable placeholder="mobile" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">phoneNumber</label>
        <el-input v-model="query.phoneNumber" clearable placeholder="phoneNumber" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">email</label>
        <el-input v-model="query.email" clearable placeholder="email" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">address</label>
        <el-input v-model="query.address" clearable placeholder="address" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">postcode</label>
        <el-input v-model="query.postcode" clearable placeholder="postcode" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">organization</label>
        <el-input v-model="query.organization" clearable placeholder="organization" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">department</label>
        <el-input v-model="query.department" clearable placeholder="department" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">position</label>
        <el-input v-model="query.position" clearable placeholder="position" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">valid</label>
        <el-input v-model="query.valid" clearable placeholder="valid" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">background</label>
        <el-input v-model="query.background" clearable placeholder="background" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="cardId" prop="cardId">
            <el-input v-model="form.cardId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="userId" prop="userId">
            <el-input v-model="form.userId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="realName" prop="realName">
            <el-input v-model="form.realName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="mobile">
            <el-input v-model="form.mobile" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="phoneNumber">
            <el-input v-model="form.phoneNumber" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="email">
            <el-input v-model="form.email" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="address">
            <el-input v-model="form.address" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="postcode">
            <el-input v-model="form.postcode" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="organization">
            <el-input v-model="form.organization" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="department">
            <el-input v-model="form.department" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="position">
            <el-input v-model="form.position" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="cardId" label="cardId" />
        <el-table-column prop="userId" label="userId" />
        <el-table-column prop="realName" label="realName" />
        <el-table-column prop="mobile" label="mobile" />
        <el-table-column prop="phoneNumber" label="phoneNumber" />
        <el-table-column prop="email" label="email" />
        <el-table-column prop="address" label="address" />
        <el-table-column prop="postcode" label="postcode" />
        <el-table-column prop="organization" label="organization" />
        <el-table-column prop="department" label="department" />
        <el-table-column prop="position" label="position" />
        <el-table-column prop="valid" label="valid" />
        <el-table-column prop="background" label="background" />
        <el-table-column v-if="checkPer(['admin','tCard:edit','tCard:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudTCard from '@/api/tCard'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { cardId: null, userId: null, realName: null, mobile: null, phoneNumber: null, email: null, address: null, postcode: null, organization: null, department: null, position: null, valid: null, background: null }
export default {
  name: 'TCard',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'SmartCard', url: 'api/tCard', idField: 'cardId', sort: 'cardId,desc', crudMethod: { ...crudTCard }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'tCard:add'],
        edit: ['admin', 'tCard:edit'],
        del: ['admin', 'tCard:del']
      },
      rules: {
        cardId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'cardId', display_name: 'cardId' },
        { key: 'userId', display_name: 'userId' },
        { key: 'realName', display_name: 'realName' },
        { key: 'mobile', display_name: 'mobile' },
        { key: 'phoneNumber', display_name: 'phoneNumber' },
        { key: 'email', display_name: 'email' },
        { key: 'address', display_name: 'address' },
        { key: 'postcode', display_name: 'postcode' },
        { key: 'organization', display_name: 'organization' },
        { key: 'department', display_name: 'department' },
        { key: 'position', display_name: 'position' },
        { key: 'valid', display_name: 'valid' },
        { key: 'background', display_name: 'background' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
