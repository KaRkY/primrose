import React from "react";
import {
  NotificationContext
} from "../../contexts";

const NotificationProvider = ({
  children,
  pushNotification,
  closeNotification,
  processNotificationQueue,
  notificationOpen,
  currentNotification: { key, ...current},
}) => (
    <NotificationContext.Provider
      value={{
        push: pushNotification,
        close: closeNotification,
        open: notificationOpen,
        current,
        key,
        exit: processNotificationQueue,
      }}
    >
      {children}
    </NotificationContext.Provider>
  );

export default NotificationProvider;