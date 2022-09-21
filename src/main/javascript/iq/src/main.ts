import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import VueI18n from 'vue-i18n';

import ByteFilter from '@/filters/byte-filter';
import PercentFilter from '@/filters/percent-filter';

import * as Dropzone from 'dropzone';

const dz = Dropzone;
dz.autoDiscover = false;

import en from '@/localizations/en';

Vue.config.productionTip = false;

Vue.use(VueI18n);
const i18n = new VueI18n({
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
  },
  dateTimeFormats: {
    en: {
      'date-time': {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      },
    },
    hu: {
      'date-time': {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      },
    },
  },
});

Vue.filter('byte', ByteFilter);
Vue.filter('percent', PercentFilter);


new Vue({
  router,
  store,
  i18n,
  render: (h) => h(App),
}).$mount('#app');
