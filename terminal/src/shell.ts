import { IDisposable, Terminal } from "@xterm/xterm";
import { parse } from "./bin/parse";

let dispose: IDisposable | undefined = undefined;
let terminal: Terminal | undefined = undefined;

export function shell(term: Terminal) {
  terminal = term
  dispose = terminal.onData(listener);
}

let cmd: string[] = [];
let path: string = "/none";

const listener = (data: string) => {
  const t = terminal!!;
  // Enter
  if (data === "\r") {
    t.write("\r\n");

    dispose?.dispose();

    try {
      if (cmd.length) {
        const [binary, ...args] = cmd.join("").split(" ");
        cmd = [];

        parse(path, t, binary, args);
      }
    } catch (e) {
      t.write(`\x1b[31m${e}\x1b[m`);
    } finally {
      t.write(`\n\r\rminshell@${path} : `);
      dispose = t.onData(listener);
    }
  }
  // Backspace
  else if (data === "\x7F") {
    cmd.pop();
    t.write("\b \b");
  }
  else {
    cmd.push(data);
    t.write(data)
  }
}