import compose from "recompose/compose";
import withHandlers from "recompose/withHandlers";
import withStateHandlers from "recompose/withStateHandlers";

export default compose(
  withStateHandlers(
    () => ({ notificationOpen: false, currentNotification: {}, notificationQueue: [] }),
    {
      pushNotification: ({ notificationQueue, ...props }) => item => ({ 
        ...props, 
        notificationQueue: [...notificationQueue, { ...item, key: new Date().getTime() }] 
      }),
      processNotificationQueue: ({ notificationQueue, ...props }) => () => {
        if (notificationQueue.length > 0) {
          const [first, ...rest] = notificationQueue;
          return ({ ...props, notificationOpen: true, currentNotification: first, notificationQueue: rest });
        } else {
          return ({ ...props, notificationOpen: false, currentNotification: {}, notificationQueue });
        }
      },
      closeNotification: ({ notificationOpen, ...props }) => (event, reason) => ({ 
        ...props, 
        notificationOpen: reason === "clickaway" ? notificationOpen : false }),
    }),
  withHandlers({
    pushNotification: ({ pushNotification, notificationOpen, processNotificationQueue }) => item => {
      pushNotification(item);
      if (!notificationOpen) {
        processNotificationQueue();
      }
    },
  }),
  
);