import { Terminal } from "@xterm/xterm";

const shouldHandleDifferently = {
  leadman: ["create", "install", "use", "default", "uninstall"]
};

async function leadmanSpecial(args: string[]) {
  switch (args[1]) {
    case "create":
      break;
    case "install":
      break;
    case "use":
      break;
    case "default":
      break;
    case "uninstall":
      break;
  }
}

export async function runBinary($path: string, args: string[], t: Terminal) {
  console.log($path, args.join(" "));
  ProcessExecutor.execute($path, args.join(" "));

  if (args.length >= 2 && args[0] == "leadman" && shouldHandleDifferently.leadman.includes(args[1])) {
    return await leadmanSpecial(args);
  }

  globalThis.readProcResp = (data) => {
    console.log(`Dat ${data}`);
    t.write(data.replace(/\n/g, "\r\n").replace(/\r\r\n/g, "\r\n"));
  }

  const resp = t.onData((data) => {
    ProcessExecutor.sendToProcess(data);
  });

  return new Promise((resolve) => {
    globalThis.procEnded = () => {
      resp.dispose();

      resolve(null);
    };
  });
}