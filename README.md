# memo2
# メモ
## ファイル検索関連
#### 検索結果からgit関連のファイルを除去
```
find . -type f | grep -Ev '^./.git'
```
#### ファイル名抽出＆拡張子除去
```
find . -type f | grep -Ev '^./.git' | sed 's!^.*/!!' | sed 's/\.[^\.]*$//'
```
#### 抽出したファイルを使用しているファイルを検索（バイナリファイルは除く）
```
find . -type f | grep -Ev '^./.git' | sed 's!^.*/!!' | sed 's/\.[^\.]*$//' | xargs -I {} sh -c 'echo "{}-------";grep --binary-files=without-match -r {} .'
```
#### 日付で検索してバックアップ
```
#バックアップディレクトリ作成
mkdir bk2020xxxx

#日付で絞り込み
find . -maxdepth 1 -type f -mtime 0 | xargs -I {} ls -l {}

#バックアップディレクトリに移動
find . -maxdepth 1 -type f -mtime 0 | xargs -I {} mv {} ./bk2020xxxx/
```

## ログの中身を検索
```
# curlとかcatの内容から検索をかける
curl -sS "http://aaa.xxx.xxx" | grep "DB_RUNRECOVERY: Fatal error" | uniq
# 上記の結果からログ先頭の[]で囲まれたサーバ名を抜き出す
curl -sS "http://aaa.xxx.xxx" | grep "DB_RUNRECOVERY: Fatal error" | uniq | sed s/"^\["/""/ | sed s/"\].*"/"" /
```

## サーバ間でファイルをdiff
```
diff < (ssh [サーバ] 'cd [ファイルのディレクトリ]; grep -Rn "" ./ | sort') < (ssh [サーバ] 'cd [ファイルのディレクトリ]; grep -Rn "" ./ | sort')

#整理版
srv1=<ホスト名1>
srv2=<ホスト名2>
cmd=grep -Rn "" ./ | sort'
diff < (ssh -o StrictHostKeyChecking=no $srv1 $cmd) < (ssh -o StrictHostKeyChecking=no $srv2 $cmd)
```
## bash_plofile
#### ssh agent設定
```
eval `ssh-agent`
```
#### ssh-addを省略
```
PW="<パスワード>"
except -c "
set timeout 5
spawn "ssh-add"
except \"Enter passphrase for\"
send \"${}\n\"
except \"$"\
exit 0
```
#### 作業ログを残す
```
#ログインのプロセス取得
P_PROC=`ps aux | grep $PPID | grep sshd | awk '{ print $11}'`
#SSHでのログインだった場合
if [ "$P_PROC" = sshd: ]; then
  #ログファイルの保存場所を変数に格納（ホストの先頭部分のみ）
  log_dir = `hostnem | cut -d "." -f 1`
  #ディレクトリがなかったら作る
  mkdir -p $logdir
  #scriptコマンドでログを記録、bash_profileの設定を読み込むためにbashを実行
  script -q -f ~/%logdir/`date "+%Y%m%d_%H%M%S"` .log -c "/bin/bash -l"
  exit 0
fi
```
#### etc
```
# 表示フォーマット設定
export PS1="\[\033[36m\][\u@\H \W]\\$ \[\033[0m\]"

# コマンドのエイリアスを設定
alias ll='ls -lrt'

# sshをラップして拡張する
function ssh2(){
  ssh -o StrictHostKeyChecking=no -A $1 -t 'export PS1="\[\033[36m\][\u@\H \W]\\$ \[\033[0m\]" && /bin/bash -l'
}

# クリップボードの内容をサーバに配置しているプログラムに送信(呼び出す)
function paster(){
  url=`pbpaste | ssh <サーバ名> "/home/〇〇/プログラム"` #pbpaste：macのクリップボードの内容
  echo "$url" | tail -n 1 | cut -d ":" -f 2- l tr -d '\n' | pbcopy
  echo "$url" | sed s/':http/\'S'\n'http/g
  open `echo "$url" | head -n 1 | cut -d ":" -f 2- | tr -d '\n'`
}

```

## xarg応用(インストールされているパッケージを検索、パッケージ名にインストールするバージョンをつけてインストール（アップデート）実行)
```
inst ls | grep <パッケージ名> | grep `inst ls <パッケージ名> | sed -e 's/.*<パッケージ名>-//g'` | sed -e 's/-.*/7.1.55/g' | tr '\n' '' | xargs -I {} -t echo 'inst i {}' | sh
```

## curl
```
#curlで各サーバにアクセスしてhtmlに保存
cat $SRVLIST | xargs -P 100 -L 1 -I {} sh -c 'curl -sS http://{} -H Host:<〇〇.co.jp> -H "user-agent: Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1" > {}.html'
#リネーム
cat $SRVLIST | xargs -P 100 -L 1 -I {} sh -c 'mv {}.html normal_{}.html'
# 保存したファイルの確認
ls -la normal*.html | cat -n
```
## .ssh/config
```
#シンプル版
Host <ホスト名>
    User <ユーザID>
    IdentityFile ~/.ssh/id_rsa

#別名版
Host <ホスト名(任意の名前)>
  HostName <ホスト名>
  User <ユーザID>
  IdentityFile ~/.ssh/id_rsa

#踏み台経由版
Host <ホスト名>
  ProxyCommand ssh.exe -A <ホスト名> nc %h %p
  User <ユーザID>
  IdentityFile ~/.ssh/id_rsa

#全ホスト共通設定
Host *
  StrictHostKeyChecking no
  HostKeyAlogorithms +ssh-dss
  AddKeyToAgent yes
  ForwardAgent yes
```
## 一般的なetc
#### リストの作成(ファイルに作成)
```
cat<<EOF>[ファイルパス]
aa
bb
cc
EOF
```
#### リストの作成(変数に作成)
```
target=`cat<<EOF
aa
bb
cc
EOF
`
echo "$target"
#リポジトリリストからまとめてクローンする
echo "$target" | xargs -I {} sh -c 'echo; echo {}; git clone git@githubxxx:/xxx/{}.git'
```
#### ローカルのknownhostから指定したホストを削除
```
ssh-keygen -R <ホスト名>
```
#### unix時間を指定の形式に変換
```
date --date "@1598844075" "+%Y/%m/%d-%H:%M:%S"
```

## 正規表現
```
#任意の文字の後にタブがある箇所を検出
.*.co.jp \t
#先頭から[]で指定した文字の箇所を検出
^[U|N|S]
#カッコで囲まれた箇所を検出
\(.*\)
#nc=xxxx&outの箇所を検出
nc=.*&out
```

## grep
```
#「word1」にひっかかるファイルからさらに「colmun1」、「colmun2」を検索
grep -rl word1 ./force-app/* | xargs -I {} sh -c 'echo; echo {}; grep -n -e .colmun1 -e .colmun2 {}'
```
