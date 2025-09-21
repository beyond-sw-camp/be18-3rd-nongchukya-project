import { createApp } from 'vue'
import App from './App.vue'
import router from './router'  
import '@mdi/font/css/materialdesignicons.css'
import vuetify from './plugins/vuetify'
import { createPinia } from 'pinia'



const app = createApp(App)
const pinia = createPinia();

app.use(router)      
app.use(vuetify)         
app.use(pinia).use(router)


import { useNotificationStore } from './stores/notifications'

const notif = useNotificationStore();
notif.init();

app.mount('#app')