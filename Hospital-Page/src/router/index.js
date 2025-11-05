import {createRouter, createWebHashHistory} from 'vue-router'
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import DoctorManage from "../views/doctor/DoctorManage.vue";
import DoctorWork from "../views/doctor/DoctorWork.vue";
import DoctorDataViz from "../views/doctor/DoctorDataViz.vue";
import PatientManage from "../views/patient/PatientManage.vue";
import BedManage from "../views/bed/BedManage.vue";
import DrugManage from "../views/drug/DrugManage.vue";
import FinanceManage from "../views/finance/FinanceManage.vue";
import TreatmentManage from "../views/treatment/TreatmentManage.vue";
import PatientDataViz from "../views/patient/PatientDataViz.vue";
import TreatmentDataViz from "../views/treatment/TreatmentDataViz.vue";
import MedicalChat from '../views/medicalmodel/MedicalChat.vue'
import BrainTumorDetection from '../views/smartmedical/BrainTumorDetection.vue'
import BlockchainWorkView from '../views/blockchain/BlockchainWorkView.vue' // 修正导入名
import DiabetesPredictionView from '../views/medical/DiabetesPredictionView.vue'

const routes = [
    {
        path: '/',
        name: "login",
        component: Login
    },
    {
        path: '/home',
        name: "home",
        component: Home,
        children: [
            {
                path: '/doctormanage',
                name: 'doctormanage',
                component: DoctorManage
            },
            {
                path: '/doctorwork',
                name: 'doctorwork',
                component: DoctorWork
            },
            {
                path: '/doctordataviz',
                name: 'doctordataviz',
                component: DoctorDataViz
            },
            {
                path: '/patientmanage',
                name: 'patientmanage',
                component: PatientManage
            },
            {
                path: '/bedmanage',
                name: 'bedmanage',
                component: BedManage
            },
            {
                path: '/drugmanage',
                name: 'drugmanage',
                component: DrugManage
            },
            {
                path: '/financemanage',
                name: 'financemanage',
                component: FinanceManage
            },
            {
                path: '/treatmentmanage',
                name: 'treatmentmanage',
                component: TreatmentManage
            },
            {
                path: '/patientdataviz',
                name: 'patientdataviz',
                component: PatientDataViz
            },
            {
                path: '/treatmentdataviz',
                name: 'treatmentdataviz',
                component: TreatmentDataViz
            },
            {
                path: '/medicalchat',
                name: 'medicalchat',
                component: MedicalChat,
                meta: {
                  title: '大模型问诊'
                }
            },
            {
                path: '/braintumordetection',
                name: 'braintumordetection',
                component: BrainTumorDetection,
                meta: {
                  title: '脑肿瘤检测'
                }
            },
            {
                path: '/blockchainwork',
                name: 'blockchainwork',
                component: BlockchainWorkView, // 修正组件名
                meta: {
                  title: '区块链排班'
                }
            },
            {
                path: '/diabetes-prediction',
                name: 'diabetes-prediction',
                component: DiabetesPredictionView,
                meta: {
                    title: '糖尿病风险预测'
                }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router