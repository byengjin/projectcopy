// async function getList(replyId) {
//
//     const result = await axios.get(`/replies/${replyId}`, {params: {page,size}})
//     if(goLast){
//         console.log(result.data)
//         return result.data;
//     }
// }
async function getList(replyId) {
    const result = await axios.get(`/replies/${replyId}`);
    console.log(result.data)
    return result.data;
}


async function addReply(replyObj, postId) {

    const response = await axios.post(`/replies/${postId}`, replyObj)
    console.log(response.data)
    return response.data
}

async function addReReply(replyObj, parentId) {

    const response = await axios.post(`/replies/${parentId}`, replyObj);
    return response.data;

}

async function getReply(replyId) {

    const response = await axios.get(`/replies/${replyId}`);
    return response.data;

}

async function modifyReply(replyId, replyObj) {
    const response = await axios.put(`/replies/${replyId}`, replyObj);
    return response.data;
}

async function removeReply(replyId) {

    const response = await axios.delete(`/replies/${replyId}`);
    return response.data;
}

function updateCharCount() {
    const replyContent = document.getElementById("replyContent");
    const charCount = document.getElementById("charCount");
    charCount.textContent = `${replyContent.value.length}/100`;
}

document.getElementById("submitReply").addEventListener("click", async () => {
    const replyContent = document.getElementById("replyContent").value.trim();

    if (replyContent.length === 0) {
        alert("댓글 내용을 입력해주세요.");
        return;
    }

    try {
        const response = await axios.post(`/replies/${postId}`, {
            content: replyContent,
        });

        if (response.status === 200) {
            alert("댓글이 등록되었습니다!");
            location.reload(); // 댓글 새로고침
        }
    } catch (error) {
        console.error("댓글 등록 실패:", error);
        alert("댓글 등록 중 오류가 발생했습니다.");
    }
});