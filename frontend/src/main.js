import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import '@mdi/font/css/materialdesignicons.css'
import vuetify from './plugins/vuetify'

// Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css'

// Bootstrap JS
import 'bootstrap/dist/js/bootstrap.bundle.min.js'


const app = createApp(App)
const pinia = createPinia();

app.use(router)      
app.use(vuetify)         
app.use(pinia).use(router)


import { useNotificationStore } from './stores/notifications'

const notif = useNotificationStore();
notif.init();

app.mount('#app')