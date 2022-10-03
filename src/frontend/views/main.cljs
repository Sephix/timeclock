(ns frontend.views.main
  (:require [frontend.components.header.core :as header]
            [frontend.views.timeclock :as timeclock]
            [frontend.views.sider :as sider]
            [frontend.components.antd.core :refer [Layout Content]]))


(defn main-panel
  []
  [Layout {:hasSider true :style {:height "100vh"} } [sider/main]
   [Layout {:class-name "site-layout" :style {:margin-left 100}} [header/main]
    [Content {:style {:height "100%" :margin "24px 16px 0" :overflow "initial"}}
     [timeclock/main]]]])
