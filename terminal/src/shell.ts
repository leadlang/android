import { IDisposable, Terminal } from "@xterm/xterm";
import { parse } from "./bin/parse";

export let dispose: IDisposable | undefined = undefined;
let terminal: Terminal | undefined = undefined;

export function shell(term: Terminal) {
  terminal = term

  if (dispose) {
    dispose.dispose();
  }

  dispose = terminal.onData(listener);
}

let cmd: string[] = [];

export let defaultPath: string = (() => {
  try {
    return Kotlin.defaultPath()
  } catch (_) {
    return ""
  }
})();

export let path: string = defaultPath

export const pathToVisible = (path: string) => {
  if (path.startsWith(defaultPath)) {
    return path.replace(defaultPath, "~")
  }

  return path
}

const listener = async (data: string) => {
  const t = terminal!!;

  console.log("Get ", data);

  // Enter
  if (data === "\r") {
    t.write("\r\n");

    dispose?.dispose();

    try {
      if (cmd.length) {
        const [binary, ...args] = cmd.join("").split(" ");
        cmd = [];

        await parse(path, (s) => path = s, t, binary, args);
      }
    } catch (e) {
      t.write(`\x1b[31m${e}\x1b[m`);
    } finally {
      t.write(`\n\r\rminshell@${pathToVisible(path)} : `);
      dispose = t.onData(listener);
    }
  }
  // Backspace
  else if (data === "\x7F") {
    if (cmd.length) {
      cmd.pop();
      t.write("\b \b");
    }
  }
  else {
    const regex = /^[a-zA-Z0-9/@^.\\ *!@#$%^&*()~+_={}|<>]+$/;
    const letters = ["-"];

    if (data.split("").every((c) => regex.test(c) || letters.includes(c))) {
      cmd.push(data);
      t.write(data)
    }
  }
}